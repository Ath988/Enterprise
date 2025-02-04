package com.bilgeadam.controller;

import com.bilgeadam.dto.request.AddCampaignRequestDto;
import com.bilgeadam.dto.request.UpdateCampaignRequestDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.entity.Campaign;
import com.bilgeadam.service.CampaignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.bilgeadam.constant.RestApis.*;

@RestController
@RequestMapping(CAMPAIGN)
@RequiredArgsConstructor
public class CampaignController {
	private final CampaignService campaignService;
	
	@PostMapping(ADDCAMPAIGN)
	public ResponseEntity<BaseResponse<Boolean>> addCampaign(@RequestBody @Valid AddCampaignRequestDto dto) {
		campaignService.addCampaign(dto);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(true)
		                                     .message("Yeni kampanya eklendi.")
		                                     .build()
		);
	}
	
	@GetMapping(GETALLCAMPAIGNS)
	public ResponseEntity<BaseResponse<List<Campaign>>> getAllCampaigns() {
		return ResponseEntity.ok(BaseResponse.<List<Campaign>>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(campaignService.getAllCampaigns())
		                                     .message("Tüm kampanyalar getirildi.")
		                                     .build()
		);
	}
	
	@GetMapping(GET_CAMPAIGN_BY_ID)
	public ResponseEntity<BaseResponse<Campaign>> getCampaignById(@RequestParam Long id) {
		return ResponseEntity.ok(BaseResponse.<Campaign>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(campaignService.getCampaignById(id))
		                                     .message("Kampanya getirildi.")
		                                     .build()
		);
	}
	
	@PutMapping(UPDATE_CAMPAIGN)
	public ResponseEntity<BaseResponse<Boolean>> updateCampaign(@RequestParam Long id, @RequestBody @Valid UpdateCampaignRequestDto dto) {
		campaignService.updateCampaign(id, dto);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(true)
		                                     .message("Kampanya güncellendi.")
		                                     .build()
		);
	}
	
	@DeleteMapping(DELETE_CAMPAIGN)
	public ResponseEntity<BaseResponse<Boolean>> deleteCampaign(@RequestParam Long id) {
		campaignService.deleteCampaign(id);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(true)
		                                     .message("Kampanya silindi.")
		                                     .build()
		);
	}
	
	@GetMapping(GET_CAMPAIGN_BY_STATUS)
	public ResponseEntity<BaseResponse<List<Campaign>>> getCampaignsByStatus(@RequestParam String status) {
		return ResponseEntity.ok(BaseResponse.<List<Campaign>>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(campaignService.getCampaignsByStatus(status))
		                                     .message("Statüye göre kampanyalar getirildi.")
		                                     .build()
		);
	}
	
	@GetMapping(GET_CAMPAIGN_BY_CUSTOMER_ID)
	public ResponseEntity<BaseResponse<List<Campaign>>> getCampaignsByCustomerId(@RequestParam Long customerId) {
		return ResponseEntity.ok(BaseResponse.<List<Campaign>>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(campaignService.getCampaignsByCustomerId(customerId))
		                                     .message("Müşteri ID'ye göre kampanyalar getirildi.")
		                                     .build()
		);
	}
	
	@GetMapping(GET_CAMPAIGN_STARTING_AFTER)
	public ResponseEntity<BaseResponse<List<Campaign>>> getCampaignsStartingAfter(@RequestParam LocalDateTime date) {
		return ResponseEntity.ok(BaseResponse.<List<Campaign>>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(campaignService.getCampaignsStartingAfter(date))
		                                     .message("Belirtilen tarihten sonra başlayan kampanyalar getirildi.")
		                                     .build()
		);
	}
	
	@GetMapping(GET_CAMPAIGN_ENDING_BEFORE)
	public ResponseEntity<BaseResponse<List<Campaign>>> getCampaignsEndingBefore(@RequestParam LocalDateTime date) {
		return ResponseEntity.ok(BaseResponse.<List<Campaign>>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(campaignService.getCampaignsEndingBefore(date))
		                                     .message("Belirtilen tarihten önce biten kampanyalar getirildi.")
		                                     .build()
		);
	}
	
	@GetMapping(GET_CAMPAIGN_BY_BUDGET_RANGE)
	public ResponseEntity<BaseResponse<List<Campaign>>> getCampaignsByBudgetRange(@RequestParam Double minBudget, @RequestParam Double maxBudget) {
		return ResponseEntity.ok(BaseResponse.<List<Campaign>>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(campaignService.getCampaignsByBudgetRange(minBudget, maxBudget))
		                                     .message("Bütçe aralığına göre kampanyalar getirildi.")
		                                     .build()
		);
	}
}