package com.bilgeadam.service;

import com.bilgeadam.dto.request.AddTicketRequestDto;
import com.bilgeadam.dto.request.UpdateTicketRequestDto;
import com.bilgeadam.entity.*;
import com.bilgeadam.entity.enums.ActivityType;
import com.bilgeadam.entity.enums.Status;
import com.bilgeadam.entity.enums.TicketStatus;
import com.bilgeadam.exception.CRMServiceException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.mapper.ActivityPerformerMapper;
import com.bilgeadam.mapper.TicketMapper;
import com.bilgeadam.repository.CustomerRepository;
import com.bilgeadam.repository.PerformerRepository;
import com.bilgeadam.repository.TicketActivityRepository;
import com.bilgeadam.repository.TicketRepository;
import com.bilgeadam.views.TicketDetailView;
import com.bilgeadam.views.VwTicket;
import com.bilgeadam.views.VwTicketActivity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {
	private final TicketRepository ticketRepository;
	private final CustomerRepository customerRepository;
	private final PerformerRepository performerRepository;
	private final TicketActivityRepository ticketActivityRepository;
	private final MailService mailService;
	private final TicketMessageService ticketMessageService;
	
	
	public void addTicket(AddTicketRequestDto dto){
		try {
			// 1️⃣ Müşteri e-posta adresinden ID al
			Customer customer = customerRepository.findByProfileEmail(dto.customerEmail())
			                                      .orElseThrow(() -> new CRMServiceException(ErrorType.CUSTOMER_NOT_FOUND));
			
			// 2️⃣ Admin'in seçtiği Performer ID ile performer bul
			Performer performer = performerRepository.findById(dto.performerId())
			                                         .orElseThrow(() -> new CRMServiceException(ErrorType.PERFORMER_NOT_FOUND));
			
			
			// 3️⃣ Ticket oluştur
			Ticket ticket = TicketMapper.INSTANCE.toTicket(dto);
			ticket.setCustomerId(customer.getCustomerId());
			ticket.setPerformerId(performer.getId());
			
			Ticket savedTicket = ticketRepository.save(ticket);
			
			// 4️⃣ TicketActivity oluştur
			TicketActivity ticketActivity = TicketMapper.INSTANCE.toTicketActivity(dto);
			ticketActivity.setTicketId(savedTicket.getId());
			ticketActivity.setPerformedBy(ActivityPerformerMapper.INSTANCE.toActivityPerformer(performer)); // 🎯 Mapper ile Performer bilgileri eklendi
			ticketActivityRepository.save(ticketActivity);
			
			// 5️⃣ **Müşterinin önceki mesajlarını bu Ticket ile ilişkilendir**
			ticketMessageService.assignMessagesToTicket(savedTicket.getId(), dto.customerEmail());
			
			// 6️⃣ Müşteriye bilgilendirme e-postası gönder ve mesaj olarak kaydet
			// 📩 **Müşteriye bilgilendirme e-postası gönder**
			String addMessage = mailService.sendTicketCreationEmail(
					savedTicket.getTicketNumber(),
					dto.subject()
			);
			
			// 📝 **Bu mesajı TicketMessage tablosuna kaydet**
			ticketMessageService.sendSystemResponse(
					"destek@enterprise.com",
					dto.customerEmail(),
					"📌 Destek Kaydınız Oluşturuldu - #" + savedTicket.getTicketNumber(),
					addMessage,
					savedTicket.getId()
			);
		} catch (Exception e) {
			throw new CRMServiceException(ErrorType.TICKET_CREATION_FAILED);
		}
	}
	
	/** 📌 Tüm Ticket'ları getirir ve her birinin TicketActivity'lerini ekler */
	public List<Ticket> getAllTickets() {
		List<Ticket> tickets = ticketRepository.findAll();
		
		// Her ticket için ilgili aktiviteleri çekip ekle
		for (Ticket ticket : tickets) {
			List<TicketActivity> activities = ticketActivityRepository.findByTicketId(ticket.getId());
			ticket.setActivities(activities);
		}
		
		return tickets;
	}
	
	/** 📌 ID'ye göre Ticket getirir ve TicketActivity'leri de ekler */
	public Ticket getTicketById(Long ticketId) {
		Ticket ticket = ticketRepository.findById(ticketId)
		                                .orElseThrow(() -> new CRMServiceException(ErrorType.TICKET_NOT_FOUND));
		
		// Ticket'a ait aktiviteleri çek ve nesneye ata
		List<TicketActivity> activities = ticketActivityRepository.findByTicketId(ticketId);
		ticket.setActivities(activities);
		
		return ticket;
	}
	
	private String getCustomerEmail(Long customerId) {
		return customerRepository.findById(customerId)
		                         .map(customer -> customer.getProfile().getEmail())
		                         .orElseThrow(() -> new CRMServiceException(ErrorType.CUSTOMER_NOT_FOUND));
	}
	
	
	public void updateTicket(Long ticketId, UpdateTicketRequestDto dto) {
		// 1️⃣ Ticket'ı ID ile bul
		Ticket ticket = ticketRepository.findById(ticketId)
		                                .orElseThrow(() -> new CRMServiceException(ErrorType.TICKET_NOT_FOUND));
		
		// 2️⃣ Eğer Ticket INACTIVE durumundaysa, önce ACTIVE yapılmalı!
		if (ticket.getStatus() == Status.INACTIVE && dto.status() != Status.ACTIVE) {
			throw new CRMServiceException(ErrorType.TICKET_INACTIVE_CANNOT_UPDATE);
		}
		
		// 3️⃣ Yeni performer atanmışsa, ID ile performer'ı bul
		Performer performer = performerRepository.findById(dto.performerId())
		                                         .orElseThrow(() -> new CRMServiceException(ErrorType.PERFORMER_NOT_FOUND));
		
		// 4️⃣ Eğer Status ACTIVE olarak güncellendiyse, artık diğer güncellemeler yapılabilir.
		if (dto.status() == Status.ACTIVE) {
			ticket.setStatus(Status.ACTIVE);
		}
		
		// 5️⃣ Ticket bilgilerini güncelle (Kapama işlemi hariç)
		TicketMapper.INSTANCE.updateTicketFromDto(dto, ticket);
		ticket.setPerformerId(performer.getId());
		
		// 6️⃣ Eğer Ticket kapatılıyorsa (CLOSED) veya çözüldüyse (RESOLVED) → `closedAt` ve `status = INACTIVE`
		if (dto.ticketStatus() == TicketStatus.CLOSED || dto.ticketStatus() == TicketStatus.RESOLVED) {
			ticket.setClosedAt(LocalDateTime.now());
			ticket.setStatus(Status.INACTIVE);
		}
		
		// 7️⃣ Güncellenmiş Ticket'ı kaydet
		ticketRepository.save(ticket);
		
		// 8️⃣ Güncelleme işlemini TicketActivity olarak kaydet
		TicketActivity ticketActivity = TicketMapper.INSTANCE.toTicketActivity(dto, ticket);
		ticketActivity.setPerformedBy(ActivityPerformerMapper.INSTANCE.toActivityPerformer(performer));
		ticketActivityRepository.save(ticketActivity);
		
		// 9️⃣ **📩 Ticket Çözüldü / Kapatıldı E-posta Gönderimi**
		if (dto.ticketStatus() == TicketStatus.RESOLVED || dto.ticketStatus() == TicketStatus.CLOSED) {
			String customerEmail = getCustomerEmail(ticket.getCustomerId());
			
			// 📩 **Müşteriye bilgilendirme e-postası gönder**
			String resolutionMessage = mailService.sendTicketResolvedEmail(
					customerEmail,
					ticket.getTicketNumber(),
					ticket.getSubject(),
					dto.ticketStatus()
			);
			
			// 📝 **Bu mesajı TicketMessage tablosuna kaydet**
			ticketMessageService.sendSystemResponse(
					"destek@enterprise.com",
					customerEmail,
					"🎉 Destek Kaydınız Güncellendi - #" + ticket.getTicketNumber(),
					resolutionMessage,
					ticket.getId()
			);
		}
	}
	
	/** 📌 ID'ye göre Ticket ve ona ait tüm TicketActivity'leri siler */
	public void deleteTicket(Long ticketId) {
		Ticket ticket = ticketRepository.findById(ticketId)
		                                .orElseThrow(() -> new CRMServiceException(ErrorType.TICKET_NOT_FOUND));
		
		// Önce ticket'ı sil
		ticketRepository.delete(ticket);
		// Sonra ticket'a bağlı tüm aktiviteleri sil
		ticketActivityRepository.deleteByTicketId(ticketId);
		
		
		
	}
}