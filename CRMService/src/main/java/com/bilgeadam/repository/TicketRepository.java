package com.bilgeadam.repository;

import com.bilgeadam.entity.Ticket;
import com.bilgeadam.entity.enums.TicketStatus;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
	
	/** 📌 Müşterinin en son oluşturulmuş Ticket'ını getir */
	Optional<Ticket> findTopByCustomerIdOrderByIdDesc(Long customerId);
	
	/** 📌 Müşteri e-posta adresine göre en son oluşturulan Ticket'ı getir */
	@Query("SELECT t FROM Ticket t WHERE t.customerId = (SELECT c.customerId FROM Customer c WHERE c.profile.email = :customerEmail) ORDER BY t.id DESC LIMIT 1")
	Optional<Ticket> findLatestTicketByCustomerEmail(@Param("customerEmail") String customerEmail);
	
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