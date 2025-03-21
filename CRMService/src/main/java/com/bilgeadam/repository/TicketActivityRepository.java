package com.bilgeadam.repository;

import com.bilgeadam.entity.TicketActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketActivityRepository extends JpaRepository<TicketActivity, Long> {
	
	List<TicketActivity> findByTicketId(Long ticketId);
	void deleteByTicketId(Long ticketId);
	
	/** 📌 Belirli bir ticket ID'ye ait aktiviteleri getir */
	List<TicketActivity> findAllByTicketId(Long ticketId);
	
	/** 📌 Belirli bir türdeki aktiviteleri getir */
	List<TicketActivity> findAllByType(com.bilgeadam.entity.enums.ActivityType type);
	
	/** 📌 Belirli bir personel ID'sine ait aktiviteleri getir */
	List<TicketActivity> findAllByPerformedBy_Id(Long performerId);
}