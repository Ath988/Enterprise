package com.bilgeadam.repository;

import com.bilgeadam.entity.Ticket;
import com.bilgeadam.entity.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
	
	/** 📌 Belirli bir müşteriye ait biletleri getir */
	List<Ticket> findAllByCustomerId(Long customerId);
	
	/** 📌 Belirli bir durumdaki biletleri getir */
	List<Ticket> findAllByStatus(com.bilgeadam.entity.enums.TicketStatus status);
	
	/** 📌 Belirli bir önceliğe sahip biletleri getir */
	List<Ticket> findAllByPriority(com.bilgeadam.entity.enums.TicketPriority priority);
	
	Optional<Ticket> findByTicketNumber(String ticketNumber);
	List<Ticket> findByCustomerId(Long customerId);
	List<Ticket> findByPerformerId(Long performerId);
	List<Ticket> findByTicketStatus(TicketStatus status);
}