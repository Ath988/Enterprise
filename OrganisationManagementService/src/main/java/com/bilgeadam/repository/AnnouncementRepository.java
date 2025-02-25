package com.bilgeadam.repository;

import com.bilgeadam.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
	List<Announcement> findByCompanyId(Long companyId);
}