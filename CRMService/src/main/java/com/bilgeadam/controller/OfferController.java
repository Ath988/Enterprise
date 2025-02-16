package com.bilgeadam.controller;

import com.bilgeadam.dto.request.AddOfferRequestDto;
import com.bilgeadam.dto.request.UpdateOfferRequestDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.service.OfferService;
import com.bilgeadam.views.VwOffer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/dev/offer")
@RequiredArgsConstructor
public class OfferController {
	private final OfferService offerService;
	
	/**
	 * 📌 Yeni bir teklif ekler.
	 */
	@PostMapping("/add-offer")
	public ResponseEntity<BaseResponse<Boolean>> createOffer(@RequestBody @Valid AddOfferRequestDto dto) {
		offerService.createOffer(dto);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(true)
		                                     .message("Yeni teklif eklendi.")
		                                     .build()
		);
	}
	
	/**
	 * 📌 Tüm teklifleri getirir.
	 */
	@GetMapping("/get-all-offers")
	public ResponseEntity<BaseResponse<List<VwOffer>>> getAllOffers() {
		List<VwOffer> allOffer = offerService.getAllOffers();
		return ResponseEntity.ok(BaseResponse.<List<VwOffer>>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(allOffer)
		                                     .message("Tüm teklifler listelendi.")
		                                     .build()
		);
	}
	
	/**
	 * 📌 Belirli bir teklifin detaylarını getirir.
	 */
	@GetMapping("/get-offer-by-id/{offerId}")
	public ResponseEntity<BaseResponse<List<VwOffer>>> getOfferById(@PathVariable Long offerId) {
		List<VwOffer> response = offerService.getOfferById(offerId);
		return ResponseEntity.ok(BaseResponse.<List<VwOffer>>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(response)
		                                     .message("Teklif detayları getirildi.")
		                                     .build()
		);
	}
	
	/**
	 * 📌 Teklifi günceller.
	 */
	@PutMapping("/update-offer/{offerId}")
	public ResponseEntity<BaseResponse<Boolean>> updateOffer(
			@PathVariable Long offerId,
			@RequestBody @Valid UpdateOfferRequestDto dto) {
		offerService.updateOffer(offerId, dto);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(true)
		                                     .message("Teklif başarıyla güncellendi.")
		                                     .build()
		);
	}
	
	/**
	 * 📌 Teklifi siler.
	 */
	@DeleteMapping("/delete-offer/{offerId}")
	public ResponseEntity<BaseResponse<Boolean>> deleteOffer(@PathVariable Long offerId) {
		offerService.deleteOffer(offerId);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(true)
		                                     .message("Teklif başarıyla silindi.")
		                                     .build()
		);
	}
}