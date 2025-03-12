package com.bilgeadam.controller;

import com.bilgeadam.dto.request.UpdatePricingPlanDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.entity.PricingPlan;
import com.bilgeadam.service.PricingPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constant.RestApis.*;


@RestController
@RequiredArgsConstructor
@RequestMapping(SUBSCRIPTION)
@CrossOrigin("*")
public class PricingPlanController {
	private final PricingPlanService pricingPlanService;
	
	@GetMapping(GET_ALL_PLANS)
	public ResponseEntity<BaseResponse<List<PricingPlan>>> getAllPlans(){
		return ResponseEntity.ok(BaseResponse.<List<PricingPlan>>builder()
				                         .data(pricingPlanService.getAllPlan())
				                         .success(true)
				                         .code(200)
				                         .message("Planlar başarılı bir şekilde getirildi")
		                                     .build());
	}
	
	
	@PreAuthorize("hasRole('SYSTEM_ADMIN')")
	@PutMapping(UPDATE_PLAN)
	public ResponseEntity<BaseResponse<PricingPlan>> updatePricingPlan(
			@RequestBody UpdatePricingPlanDto dto) {
		
		PricingPlan updated = pricingPlanService.updatePricingPlan(dto);
		
		return ResponseEntity.ok(BaseResponse.<PricingPlan>builder()
		                                     .data(updated)
		                                     .success(true)
		                                     .code(200)
		                                     .message("Plan başarıyla güncellendi")
		                                     .build());
	}
	
	
}