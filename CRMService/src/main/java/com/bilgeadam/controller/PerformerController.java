package com.bilgeadam.controller;

import com.bilgeadam.dto.request.AddPerformerRequestDto;
import com.bilgeadam.dto.request.UpdatePerformerRequestDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.entity.Performer;
import com.bilgeadam.service.PerformerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/dev/performer")
@RequiredArgsConstructor
public class PerformerController {
	
	private final PerformerService performerService;
	
	/** 📌 Yeni bir Performer ekler */
	@PostMapping("add")
	public ResponseEntity<BaseResponse<Boolean>> addPerformer(@RequestBody @Valid AddPerformerRequestDto dto) {
		performerService.addPerformer(dto);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(true)
		                                     .message("Yeni performer başarıyla eklendi.")
		                                     .build()
		);
	}
	
	/** 📌 Tüm Performer'ları getirir */
	@GetMapping("all")
	public ResponseEntity<BaseResponse<List<Performer>>> getAllPerformers() {
		List<Performer> performers = performerService.getAllPerformers();
		return ResponseEntity.ok(BaseResponse.<List<Performer>>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(performers)
		                                     .message("Tüm performerlar listelendi.")
		                                     .build()
		);
	}
	
	/** 📌 ID'ye göre Performer getirir */
	@GetMapping("id" + "/{id}")
	public ResponseEntity<BaseResponse<Performer>> getPerformerById(@PathVariable Long id) {
		Performer performer = performerService.getPerformerById(id);
		return ResponseEntity.ok(BaseResponse.<Performer>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(performer)
		                                     .message("Performer bilgisi getirildi.")
		                                     .build()
		);
	}
	
	/** 📌 E-posta adresine göre Performer getirir */
	@GetMapping("email")
	public ResponseEntity<BaseResponse<Performer>> getPerformerByEmail(@RequestParam String email) {
		Performer performer = performerService.getPerformerByEmail(email);
		return ResponseEntity.ok(BaseResponse.<Performer>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(performer)
		                                     .message("E-posta adresine göre performer getirildi.")
		                                     .build()
		);
	}
	
	@PutMapping("update" + "/{performerId}")
	public ResponseEntity<BaseResponse<Boolean>> updatePerformer(
			@PathVariable Long performerId,
			@RequestBody @Valid UpdatePerformerRequestDto dto) {
		performerService.updatePerformer(performerId, dto);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(true)
		                                     .message("Performer bilgileri güncellendi.")
		                                     .build());
	}
	
	/** 📌 ID'ye göre Performer siler */
	@DeleteMapping("delete" + "/{id}")
	public ResponseEntity<BaseResponse<Boolean>> deletePerformer(@PathVariable Long id) {
		performerService.deletePerformer(id);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(true)
		                                     .message("Performer başarıyla silindi.")
		                                     .build()
		);
	}
	
	@DeleteMapping("delete-performers")
	public ResponseEntity<BaseResponse<Boolean>> deleteCustomers(@RequestBody List<Long> performerIds) {
		performerService.deletePerformers(performerIds);
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
		                                     .code(200)
		                                     .success(true)
		                                     .data(true)
		                                     .message("Seçili personeller başarıyla silindi.")
		                                     .build());
	}
}