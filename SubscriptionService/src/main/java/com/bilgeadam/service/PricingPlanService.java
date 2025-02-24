package com.bilgeadam.service;

import com.bilgeadam.dto.request.UpdatePricingPlanDto;
import com.bilgeadam.entity.PricingPlan;
import com.bilgeadam.repository.PricingPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PricingPlanService {
	private final PricingPlanRepository pricingPlanRepository;
	
	
	public List<PricingPlan> getAllPlan() {
		return pricingPlanRepository.findAll();
	}
	
	public PricingPlan updatePricingPlan(UpdatePricingPlanDto dto) {
		PricingPlan existingPlan = pricingPlanRepository.findById(dto.id())
		                                                .orElseThrow(() -> new RuntimeException("Pricing plan not found"));
		
		existingPlan.setTitle(dto.title());
		existingPlan.setPrice(dto.price());
		existingPlan.setFeatures(dto.features());
		
		return pricingPlanRepository.save(existingPlan);
	}
	
}