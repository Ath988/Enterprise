package com.bilgeadam.repository;

import com.bilgeadam.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
	List<Announcement> findByCompanyId(Long companyId);
	@Query("SELECT a FROM Announcement a WHERE a.id IN (" +
			"SELECT ar.announcementId FROM AnnouncementIsRead ar WHERE ar.receiverId = :employeeId AND ar.isRead = true)")
	List<Announcement> findReadAnnouncementsByEmployeeId(@Param("employeeId") Long employeeId);

	@Query("SELECT a FROM Announcement a WHERE a.id IN (" +
			"SELECT ar.announcementId FROM AnnouncementIsRead ar WHERE ar.receiverId = :employeeId AND ar.isRead = false)")
	List<Announcement> findUnreadAnnouncementsByEmployeeId(@Param("employeeId") Long employeeId);
}