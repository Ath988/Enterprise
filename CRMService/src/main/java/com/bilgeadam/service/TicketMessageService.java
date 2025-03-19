package com.bilgeadam.service;

import com.bilgeadam.dto.request.AddTicketMessageRequestDto;
import com.bilgeadam.entity.Ticket;
import com.bilgeadam.entity.TicketMessage;
import com.bilgeadam.exception.CRMServiceException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.repository.TicketMessageRepository;
import com.bilgeadam.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketMessageService {
	private final TicketRepository ticketRepository;
	private final TicketMessageRepository ticketMessageRepository;
	private final MailService mailService;
	
	/** 📌 Müşteri mesajını kaydeder ve destek ekibini bilgilendirir */
	public void submitTicketMessage(AddTicketMessageRequestDto dto) {
		
		// 1️⃣ TicketMessage nesnesi oluştur
		TicketMessage ticketMessage = TicketMessage.builder()
		                                           .ticketId(null)
		                                           .senderEmail(dto.senderEmail())  // Müşterinin e-posta adresi
		                                           .recipientEmail(dto.recipientEmail()) // Destek ekibinin e-posta adresi
		                                           .subject(dto.subject())
		                                           .messageContent(dto.messageContent())
		                                           .sentAt(LocalDateTime.now())
		                                           .build();
		
		// 2️⃣ Mesajı veritabanına kaydet
		ticketMessageRepository.save(ticketMessage);
		
		// 3️⃣ Destek ekibine mesajı e-posta ile gönder
		mailService.sendSupportTeamNotificationEmail(
				dto.senderEmail(),
				dto.recipientEmail(),
				dto.subject(),
				dto.messageContent()
		);
		
		// 1️⃣ **Müşteriye ait en son Ticket'ı bul**
		Optional<Ticket> latestTicketOpt = ticketRepository.findLatestTicketByCustomerEmail(dto.senderEmail());
		
		// 2️⃣ Ticket varsa ona bağla, yoksa serbest mesaj olarak kaydet
		Long ticketId = latestTicketOpt.map(Ticket::getId).orElse(null);
		
		// 4️⃣ **Müşteriye otomatik yanıt gönder ve mesajı kaydet**
		String autoReplyContent = mailService.sendTicketMessageReceivedEmail(
				dto.senderEmail(),
				dto.subject(),
				dto.messageContent()
		);
		
		TicketMessage autoReplyMessage = TicketMessage.builder()
		                                              .ticketId(null) // ❌ TICKET ATAMASI YOK
		                                              .senderEmail(dto.recipientEmail())  // **Destek ekibinin e-posta adresi**
		                                              .recipientEmail(dto.senderEmail())  // **Müşterinin e-posta adresi**
		                                              .subject(dto.subject())
		                                              .messageContent(autoReplyContent)  // **Otomatik yanıt içeriği**
		                                              .sentAt(LocalDateTime.now())
		                                              .build();
		
		ticketMessageRepository.save(autoReplyMessage);
	}
	
	/** 📌 Sistem tarafından müşteriye yanıt gönderildiğinde mesajı kaydeder */
	public void sendSystemResponse(String senderEmail, String recipientEmail, String subject, String messageContent, Long ticketId) {
		// 1️⃣ Yanıt mesajını oluştur
		TicketMessage responseMessage = TicketMessage.builder()
		                                             .ticketId(ticketId)
		                                             .senderEmail(senderEmail)
		                                             .recipientEmail(recipientEmail)
		                                             .subject(subject)
		                                             .messageContent(messageContent)
		                                             .sentAt(java.time.LocalDateTime.now())
		                                             .build();
		
		// 2️⃣ Yanıt mesajını kaydet
		ticketMessageRepository.save(responseMessage);
		
		// 3️⃣ Müşteriye e-posta gönder
		mailService.sendEmail(recipientEmail, subject, messageContent);
	}
	
	/** 📌 Ticket oluşturulduğunda müşteri mesajlarını ve giden otomatik mesajları bu Ticket'a bağlar */
	public void assignMessagesToTicket(Long ticketId, String customerEmail) {
		// 1️⃣ **Müşteri tarafından gönderilmiş ticket'siz mesajları bul**
		List<TicketMessage> customerMessages = ticketMessageRepository.findBySenderEmailAndTicketIdIsNull(customerEmail);
		
		// 2️⃣ **Destek ekibinden müşteriye gönderilen ancak ticket'siz mesajları bul**
		List<TicketMessage> supportMessages = ticketMessageRepository.findByRecipientEmailAndTicketIdIsNull(customerEmail);
		
		// 3️⃣ **Tüm mesajları tek listede birleştir**
		List<TicketMessage> allMessages = new ArrayList<>();
		allMessages.addAll(customerMessages);
		allMessages.addAll(supportMessages);
		
		// 4️⃣ **Eğer güncellenecek mesajlar varsa, ticketId atayarak güncelle**
		if (!allMessages.isEmpty()) {
			for (TicketMessage message : allMessages) {
				message.setTicketId(ticketId); // ✅ Yeni oluşturulan ticket ile eşleştir
			}
			ticketMessageRepository.saveAll(allMessages); // **Toplu kayıt güncellemesi**
		}
	}
	
}