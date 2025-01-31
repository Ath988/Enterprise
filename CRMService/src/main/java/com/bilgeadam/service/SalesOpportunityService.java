package com.bilgeadam.service;

import com.bilgeadam.dto.request.AddSalesOpportunityRequestDto;
import com.bilgeadam.dto.request.UpdateSalesOpportunityRequestDto;
import com.bilgeadam.entity.SalesOpportunity;
import com.bilgeadam.exception.CRMServiceException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.mapper.SalesOpportunityMapper;
import com.bilgeadam.repository.SalesOpportunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesOpportunityService {
	private final SalesOpportunityRepository salesOpportunityRepository;
	
	public void addSalesOpportunity(AddSalesOpportunityRequestDto dto) {
		SalesOpportunity salesOpportunity = SalesOpportunityMapper.INSTANCE.fromAddSalesOpportunityDto(dto);
		salesOpportunityRepository.save(salesOpportunity);
	}
	
	public List<SalesOpportunity> getAllSalesOpportunities() {
		return salesOpportunityRepository.findAll();
	}
	
	public SalesOpportunity getSalesOpportunityById(Long id) {
		return salesOpportunityRepository.findById(id)
		                                 .orElseThrow(() -> new CRMServiceException(ErrorType.SALES_OPPORTUNITY_NOT_FOUND));
	}
	
	public void updateSalesOpportunity(Long id, UpdateSalesOpportunityRequestDto dto) {
		SalesOpportunity salesOpportunity = salesOpportunityRepository.findById(id)
		                                                              .orElseThrow(() -> new CRMServiceException(ErrorType.SALES_OPPORTUNITY_NOT_FOUND));
		SalesOpportunityMapper.INSTANCE.updateSalesOpportunityFromDto(dto, salesOpportunity);
		salesOpportunityRepository.save(salesOpportunity);
	}
	
	public void deleteSalesOpportunity(Long id) {
		SalesOpportunity salesOpportunity = salesOpportunityRepository.findById(id)
		                                                              .orElseThrow(() -> new CRMServiceException(ErrorType.SALES_OPPORTUNITY_NOT_FOUND));
		salesOpportunityRepository.delete(salesOpportunity);
	}
	
	public List<SalesOpportunity> getSalesOpportunitiesByCustomerId(Long customerId) {
		return salesOpportunityRepository.findByCustomerId(customerId);
	}
	
	public List<SalesOpportunity> getSalesOpportunitiesByCampaignId(Long campaignId) {
		return salesOpportunityRepository.findByCampaignId(campaignId);
	}
	
	public List<SalesOpportunity> getSalesOpportunitiesByEstimatedValueRange(Double minValue, Double maxValue) {
		return salesOpportunityRepository.findByEstimatedValueBetween(minValue, maxValue);
	}
	
	public List<SalesOpportunity> getSalesOpportunitiesByStatus(String status) {
		return salesOpportunityRepository.findByStatus(status);
	}
	
	public List<SalesOpportunity> getSalesOpportunitiesByCustomerAndCampaign(Long customerId, Long campaignId) {
		return salesOpportunityRepository.findByCustomerIdAndCampaignId(customerId, campaignId);
	}
}