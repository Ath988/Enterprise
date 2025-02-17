package com.bilgeadam.repository;

import com.bilgeadam.entity.Ticket;
import com.bilgeadam.views.VwTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
	
	/** 📌 Belirli bir müşteriye ait biletleri getir */
	List<Ticket> findAllByCustomerId(Long customerId);
	
	/** 📌 Belirli bir durumdaki biletleri getir */
	List<Ticket> findAllByStatus(com.bilgeadam.entity.enums.TicketStatus status);
	
	/** 📌 Belirli bir önceliğe sahip biletleri getir */
	List<Ticket> findAllByPriority(com.bilgeadam.entity.enums.TicketPriority priority);
	
	@Query("""
    SELECT new com.bilgeadam.views.VwTicket(
        t.subject,
        t.status,
        t.priority,
        t.createdAt,
        CONCAT(c.profile.firstName, ' ', c.profile.lastName),
        c.profile.email
    )
    FROM Ticket t
    LEFT JOIN Customer c ON t.customerId = c.customerId
    WHERE t.id = :ticketId
""")
	VwTicket findTicketWithCustomerInfoById(Long ticketId);
	
	@Query("""
    SELECT new com.bilgeadam.views.VwTicket(
        t.subject,
        t.status,
        t.priority,
        t.createdAt,
        CONCAT(c.profile.firstName, ' ', c.profile.lastName),
        c.profile.email
    )
    FROM Ticket t
    LEFT JOIN Customer c ON t.customerId = c.customerId
""")
	List<VwTicket> findAllTicketsWithCustomerInfo();
}