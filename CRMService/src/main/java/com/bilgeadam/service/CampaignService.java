package com.bilgeadam.service;

import com.bilgeadam.dto.request.AddCampaignRequestDto;
import com.bilgeadam.dto.request.UpdateCampaignRequestDto;
import com.bilgeadam.entity.Campaign;
import com.bilgeadam.exception.CRMServiceException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.mapper.CampaignMapper;
import com.bilgeadam.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CampaignService {
	private final CampaignRepository campaignRepository;
	
	public void addCampaign(AddCampaignRequestDto dto){
		Campaign campaign = CampaignMapper.INSTANCE.fromAddCampaignDto(dto);
		campaignRepository.save(campaign);
	}
	public List<Campaign> getAllCampaigns(){
		return campaignRepository.findAll();
	}
	public Campaign getCampaignById(Long id){
		return campaignRepository.findById(id)
				.orElseThrow(()-> new CRMServiceException(ErrorType.CAMPAIGN_NOT_FOUND));
	}
	public void updateCampaign(Long id, UpdateCampaignRequestDto dto){
		Campaign campaign = campaignRepository.findById(id)
				.orElseThrow(()-> new CRMServiceException(ErrorType.CAMPAIGN_NOT_FOUND));
		CampaignMapper.INSTANCE.updateCampaignFromDto(dto, campaign);
		campaignRepository.save(campaign);
	}
	
	public void deleteCampaign(Long id){
		Campaign campaign = campaignRepository.findById(id)
		                                      .orElseThrow(()-> new CRMServiceException(ErrorType.CAMPAIGN_NOT_FOUND));
		campaignRepository.delete(campaign);
	}
	
	public List<Campaign> getCampaignsByStatus(String status) {
		return campaignRepository.findByStatus(status);
	}
	
	public List<Campaign> getCampaignsByCustomerId(Long customerId) {
		return campaignRepository.findByCustomerId(customerId);
	}
	
	public List<Campaign> getCampaignsStartingAfter(LocalDateTime date) {
		return campaignRepository.findByStartDateAfter(date);
	}
	
	public List<Campaign> getCampaignsEndingBefore(LocalDateTime date) {
		return campaignRepository.findByEndDateBefore(date);
	}
	
	public List<Campaign> getCampaignsByBudgetRange(Double minBudget, Double maxBudget) {
		return campaignRepository.findByBudgetBetween(minBudget, maxBudget);
	}
}