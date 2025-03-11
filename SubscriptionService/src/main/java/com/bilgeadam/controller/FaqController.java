package com.bilgeadam.controller;

import com.bilgeadam.dto.request.UpdateFaqDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.entity.Faq;
import com.bilgeadam.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constant.RestApis.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(FAQ)
@CrossOrigin("*")
public class FaqController {
	private final FaqService faqService;
	
	@GetMapping(GET_ALL_FAQ)
	public ResponseEntity<BaseResponse<List<Faq>>> getAllFaqs(){
		return ResponseEntity.ok(BaseResponse.<List<Faq>>builder()
				                         .success(true)
				                         .code(200)
				                         .message("sorular başarıyla getirildi")
				                         .data(faqService.getAllFaqs())
		                                     .build());
	}
	@PreAuthorize("hasRole('SYSTEM_ADMIN')")
	@PostMapping(CREATE_FAQ)
	public ResponseEntity<BaseResponse<Boolean>> createFaq(String question){
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
				                         .data(faqService.createFaq(question))
				                         .code(200)
				                         .message("soru başarıyla oluşturuldu")
				                         .success(true)
		                                     .build());
	}
	@PreAuthorize("hasRole('SYSTEM_ADMIN')")
	@PutMapping(UPDATE_FAQ)
	public ResponseEntity<BaseResponse<Faq>> updateFaq(@RequestBody UpdateFaqDto dto){
		Faq updated = faqService.updateFaq(dto);
		return ResponseEntity.ok(BaseResponse.<Faq>builder()
				                         .success(true)
				                         .data(updated)
				                         .code(200)
				                         .message("soru başarıyla guncellendi")
		                                     .build());
	}
	@PreAuthorize("hasRole('SYSTEM_ADMIN')")
	@DeleteMapping(CANCEL_FAQ)
	public ResponseEntity<BaseResponse<Boolean>> deleteFaq(Long faqId){
	return ResponseEntity.ok(BaseResponse.<Boolean>builder()
			                         .message("soru başarıyla  silindi")
			                         .success(true)
			                         .data(faqService.deleteFaq(faqId))
			                         .code(200)
	                                     .build());
	}
}