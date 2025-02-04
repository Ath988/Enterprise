package com.bilgeadam.repository;

import com.bilgeadam.entity.SupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
	/**
	 * Müşteri ID'ye göre destek taleplerini getirir.
	 */
	List<SupportTicket> findByCustomerId(Long customerId);
	
	/**
	 * Belirli bir statüdeki destek taleplerini getirir.
	 */
	List<SupportTicket> findByStatus(String status);
	
	/**
	 * Belirli bir müşteri için belirtilen konuya sahip destek taleplerini getirir.
	 */
	List<SupportTicket> findByCustomerIdAndSubject(Long customerId, String subject);
	
	/**
	 * Destek taleplerini oluşturulma zamanına göre sıralayarak getirir.
	 */
	List<SupportTicket> findTop10ByOrderByCreatedAtDesc();
}