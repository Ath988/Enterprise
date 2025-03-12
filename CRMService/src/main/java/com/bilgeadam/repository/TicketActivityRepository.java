package com.bilgeadam.repository;

import com.bilgeadam.entity.TicketActivity;
import com.bilgeadam.views.VwTicketActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketActivityRepository extends JpaRepository<TicketActivity, Long> {
	
	/** 📌 Belirli bir ticket ID'ye ait aktiviteleri getir */
	List<TicketActivity> findAllByTicketId(Long ticketId);
	
	/** 📌 Belirli bir türdeki aktiviteleri getir */
	List<TicketActivity> findAllByType(com.bilgeadam.entity.enums.ActivityType type);
	
	/** 📌 Belirli bir personel ID'sine ait aktiviteleri getir */
	List<TicketActivity> findAllByPerformedBy_Id(Long performerId);
	
	@Query("""
    SELECT new com.bilgeadam.views.VwTicketActivity(
        a.type,
        a.timestamp,
        a.performedBy.name,
        a.performedBy.staff,
        a.content
    )
    FROM TicketActivity a
    WHERE a.ticketId = :ticketId
""")
	List<VwTicketActivity> findActivitiesByTicketId(Long ticketId);
	
	/*@Query("""
    SELECT new com.bilgeadam.views.VwTicketActivity(
        a.type,
        a.timestamp,
        a.performedBy.name,
        a.performedBy.staff,
        a.content
    )
    FROM TicketActivity a
""")
	List<VwTicketActivity> findActivitiesByTicket(Long ticketId);*/
}