package com.bilgeadam.repository;

import com.bilgeadam.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
	/**
	 * Belirtilen statüye sahip kampanyaları getirir.
	 */
	List<Campaign> findByStatus(String status);
	
	/**
	 * Belirtilen müşteri ID'sine ait kampanyaları getirir.
	 */
	List<Campaign> findByCustomerId(Long customerId);
	
	/**
	 * Belirtilen tarihten sonra başlayan kampanyaları getirir.
	 */
	List<Campaign> findByStartDateAfter(LocalDateTime date);
	
	/**
	 * Belirtilen tarihten önce biten kampanyaları getirir.
	 */
	List<Campaign> findByEndDateBefore(LocalDateTime date);
	
	/**
	 * Belirtilen bütçe aralığında yer alan kampanyaları getirir.
	 */
	List<Campaign> findByBudgetBetween(Double minBudget, Double maxBudget);
}