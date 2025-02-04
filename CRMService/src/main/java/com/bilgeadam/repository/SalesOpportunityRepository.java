package com.bilgeadam.repository;

import com.bilgeadam.entity.SalesOpportunity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalesOpportunityRepository extends JpaRepository<SalesOpportunity, Long> {
	/**
	 * Belirtilen müşteri ID'sine ait satış fırsatlarını getirir.
	 */
	List<SalesOpportunity> findByCustomerId(Long customerId);
	
	/**
	 * Belirtilen kampanya ID'sine ait satış fırsatlarını getirir.
	 */
	List<SalesOpportunity> findByCampaignId(Long campaignId);
	
	/**
	 * Belirtilen değer aralığındaki satış fırsatlarını getirir.
	 */
	List<SalesOpportunity> findByEstimatedValueBetween(Double minValue, Double maxValue);
	
	/**
	 * Açık durumda olan satış fırsatlarını getirir.
	 */
	List<SalesOpportunity> findByStatus(String status);
	
	/**
	 * Belirtilen müşteri ve kampanyaya ait satış fırsatlarını getirir.
	 */
	List<SalesOpportunity> findByCustomerIdAndCampaignId(Long customerId, Long campaignId);
}