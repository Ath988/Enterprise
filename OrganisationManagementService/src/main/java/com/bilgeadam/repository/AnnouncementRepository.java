package com.bilgeadam.repository;

import com.bilgeadam.dto.response.AnnouncementReadResponseDto;
import com.bilgeadam.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
	List<Announcement> findByCompanyId(Long companyId);

	@Query("SELECT NEW com.bilgeadam.dto.response.AnnouncementReadResponseDto(a.id, a.title, a.content, ar.isRead, a.creationDate) " +
			"FROM Announcement a, AnnouncementIsRead ar " +
			"WHERE a.id = ar.announcementId " +
			"AND ar.receiverId = :employeeId AND ar.isRead = true")
	List<AnnouncementReadResponseDto> findReadAnnouncementsByEmployeeId(@Param("employeeId") Long employeeId);

	@Query("SELECT NEW com.bilgeadam.dto.response.AnnouncementReadResponseDto(a.id, a.title, a.content, ar.isRead, a.creationDate) " +
			"FROM Announcement a, AnnouncementIsRead ar " +
			"WHERE a.id = ar.announcementId " +
			"AND ar.receiverId = :employeeId AND ar.isRead = false")
	List<Announcement> findUnreadAnnouncementsByEmployeeId(@Param("employeeId") Long employeeId);
}