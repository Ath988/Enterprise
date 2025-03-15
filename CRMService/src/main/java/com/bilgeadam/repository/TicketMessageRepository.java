package com.bilgeadam.repository;

import com.bilgeadam.entity.TicketMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketMessageRepository extends JpaRepository<TicketMessage, Long> {
	
	/** 📌 **Müşteri tarafından gönderilen ancak henüz bir Ticket'a bağlanmamış mesajları getirir** */
	List<TicketMessage> findBySenderEmailAndTicketIdIsNull(String senderEmail);
	
	/** 📌 **Destek ekibinden müşteriye giden ancak henüz bir Ticket'a bağlanmamış mesajları getirir** */
	List<TicketMessage> findByRecipientEmailAndTicketIdIsNull(String recipientEmail);
}