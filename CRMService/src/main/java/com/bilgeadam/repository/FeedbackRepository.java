package com.bilgeadam.repository;

import com.bilgeadam.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
	/** 📌 Belirli bir Ticket ID'ye ait tüm geri bildirimleri getir */
	List<Feedback> findByTicketId(Long ticketId);
	
	/** 📌 Belirli bir e-posta adresine ait geri bildirimleri getir */
	List<Feedback> findByEmail(String email);
	
	/** 📌 ID ile geri bildirim bul */
	Optional<Feedback> findById(Long id);
}