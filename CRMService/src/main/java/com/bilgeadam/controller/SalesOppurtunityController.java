package com.bilgeadam.controller;

import com.bilgeadam.dto.request.AddSalesOpportunityRequestDto;
import com.bilgeadam.dto.request.UpdateSalesOpportunityRequestDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.entity.SalesOpportunity;
import com.bilgeadam.service.SalesOpportunityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constant.RestApis.*;

@RestController
@RequestMapping(SALES_OPPORTUNITY)
@RequiredArgsConstructor
public class SalesOppurtunityController {
	private final SalesOpportunityService salesOpportunityService;
	
	@PostMapping(ADD_SALES_OPPORTUNITY)
	public ResponseEntity<BaseResponse<Boolean>> addSalesOpportunity(@RequestBody @Valid AddSalesOpportunityRequestDto dto) {
		salesOpportunityService.addSalesOpportunity(dto);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(true)
		                                     .message("Yeni satış fırsatı eklendi.")
		                                     .build()
		);
	}
	
	@GetMapping(GET_ALL_SALES_OPPORTUNITIES)
	public ResponseEntity<BaseResponse<List<SalesOpportunity>>> getAllSalesOpportunities() {
		return ResponseEntity.ok(BaseResponse.<List<SalesOpportunity>>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(salesOpportunityService.getAllSalesOpportunities())
		                                     .message("Tüm satış fırsatları getirildi.")
		                                     .build()
		);
	}
	
	@GetMapping(GET_SALES_OPPORTUNITY_BY_ID)
	public ResponseEntity<BaseResponse<SalesOpportunity>> getSalesOpportunityById(@RequestParam Long id) {
		return ResponseEntity.ok(BaseResponse.<SalesOpportunity>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(salesOpportunityService.getSalesOpportunityById(id))
		                                     .message("Satış fırsatı getirildi.")
		                                     .build()
		);
	}
	
	@PutMapping(UPDATE_SALES_OPPORTUNITY)
	public ResponseEntity<BaseResponse<Boolean>> updateSalesOpportunity(@RequestParam Long id, @RequestBody @Valid UpdateSalesOpportunityRequestDto dto) {
		salesOpportunityService.updateSalesOpportunity(id, dto);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(true)
		                                     .message("Satış fırsatı güncellendi.")
		                                     .build()
		);
	}
	
	@DeleteMapping(DELETE_SALES_OPPORTUNITY)
	public ResponseEntity<BaseResponse<Boolean>> deleteSalesOpportunity(@RequestParam Long id) {
		salesOpportunityService.deleteSalesOpportunity(id);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(true)
		                                     .message("Satış fırsatı silindi.")
		                                     .build()
		);
	}
	
	@GetMapping(GET_SALES_OPPORTUNITIES_BY_CUSTOMER_ID)
	public ResponseEntity<BaseResponse<List<SalesOpportunity>>> getSalesOpportunitiesByCustomerId(@RequestParam Long customerId) {
		return ResponseEntity.ok(BaseResponse.<List<SalesOpportunity>>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(salesOpportunityService.getSalesOpportunitiesByCustomerId(customerId))
		                                     .message("Müşteri ID'ye göre satış fırsatları getirildi.")
		                                     .build()
		);
	}
	
	@GetMapping(GET_SALES_OPPORTUNITIES_BY_CAMPAIGN_ID)
	public ResponseEntity<BaseResponse<List<SalesOpportunity>>> getSalesOpportunitiesByCampaignId(@RequestParam Long campaignId) {
		return ResponseEntity.ok(BaseResponse.<List<SalesOpportunity>>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(salesOpportunityService.getSalesOpportunitiesByCampaignId(campaignId))
		                                     .message("Kampanya ID'ye göre satış fırsatları getirildi.")
		                                     .build()
		);
	}
	
	@GetMapping(GET_SALES_OPPORTUNITIES_BY_ESTIMATED_VALUE_RANGE)
	public ResponseEntity<BaseResponse<List<SalesOpportunity>>> getSalesOpportunitiesByEstimatedValueRange(@RequestParam Double minValue, @RequestParam Double maxValue) {
		return ResponseEntity.ok(BaseResponse.<List<SalesOpportunity>>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(salesOpportunityService.getSalesOpportunitiesByEstimatedValueRange(minValue, maxValue))
		                                     .message("Tahmini değer aralığında satış fırsatları getirildi.")
		                                     .build()
		);
	}
	
	@GetMapping(GET_SALES_OPPORTUNITIES_BY_STATUS)
	public ResponseEntity<BaseResponse<List<SalesOpportunity>>> getSalesOpportunitiesByStatus(@RequestParam String status) {
		return ResponseEntity.ok(BaseResponse.<List<SalesOpportunity>>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(salesOpportunityService.getSalesOpportunitiesByStatus(status))
		                                     .message("Statüye göre satış fırsatları getirildi.")
		                                     .build()
		);
	}
	
	@GetMapping(GET_SALES_OPPORTUNITIES_BY_CUSTOMER_AND_CAMPAIGN)
	public ResponseEntity<BaseResponse<List<SalesOpportunity>>> getSalesOpportunitiesByCustomerAndCampaign(@RequestParam Long customerId, @RequestParam Long campaignId) {
		return ResponseEntity.ok(BaseResponse.<List<SalesOpportunity>>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(salesOpportunityService.getSalesOpportunitiesByCustomerAndCampaign(customerId, campaignId))
		                                     .message("Müşteri ve kampanya ID'ye göre satış fırsatları getirildi.")
		                                     .build()
		);
	}
}