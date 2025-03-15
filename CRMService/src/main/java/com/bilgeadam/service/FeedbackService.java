package com.bilgeadam.service;

import com.bilgeadam.dto.request.AddFeedbackRequestDto;
import com.bilgeadam.entity.Feedback;
import com.bilgeadam.entity.Ticket;
import com.bilgeadam.entity.enums.TicketStatus;
import com.bilgeadam.exception.CRMServiceException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.mapper.FeedbackMapper;
import com.bilgeadam.repository.FeedbackRepository;
import com.bilgeadam.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class FeedbackService {
	
	private final FeedbackRepository feedbackRepository;
	private final TicketRepository ticketRepository;
	private final MailService mailService;
	
	public Feedback submitFeedback(Long ticketId, AddFeedbackRequestDto dto) {
		
		// 1️⃣ Ticket var mı ve uygun durumda mı kontrol et
		Ticket ticket = ticketRepository.findById(ticketId)
		                                .orElseThrow(() -> new CRMServiceException(ErrorType.TICKET_NOT_FOUND));
		
		if (!(ticket.getTicketStatus() == TicketStatus.RESOLVED || ticket.getTicketStatus() == TicketStatus.CLOSED)) {
			throw new CRMServiceException(ErrorType.FEEDBACK_NOT_ALLOWED);
		}
		
		// 2️⃣ Feedback DTO'dan Entity'ye dönüştürülüyor
		Feedback feedback = FeedbackMapper.INSTANCE.fromAddFeedbackRequestDto(dto);
		feedback.setTicketId(ticketId);
		
		// 3️⃣ Geri bildirim kaydediliyor
		Feedback savedFeedback = feedbackRepository.save(feedback);
		
		mailService.sendFeedbackToSupport(feedback.getEmail(), feedback.getSubject(), feedback.getMessage());
		
		// 4️⃣ Müşteriye geri bildirim alındı e-postası gönder
		mailService.sendFeedbackReceivedEmail(feedback.getEmail());
		
		return savedFeedback;
	}
	
	/** 📌 Ticket ID'ye Göre Geri Bildirimleri Getirme */
	public List<Feedback> getFeedbackByTicketId(Long ticketId) {
		return feedbackRepository.findByTicketId(ticketId);
	}
	
	/** 📌 E-posta Adresine Göre Geri Bildirimleri Getirme */
	public List<Feedback> getFeedbackByEmail(String email) {
		return feedbackRepository.findByEmail(email);
	}
}