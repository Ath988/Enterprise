package com.bilgeadam.controller;

import com.bilgeadam.dto.request.SubmitSurveyRequestDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.dto.response.SurveyResponseDetailDto;
import com.bilgeadam.entity.SurveyResponse;
import com.bilgeadam.service.SurveyResponseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constant.Apis.*;
@RequiredArgsConstructor
@RequestMapping(SURVEY_RESPONSE)
@RestController
@CrossOrigin("*")
public class SurveyResponseController {
	
	private final SurveyResponseService surveyResponseService;
	
	@PostMapping(SUBMIT_SURVEY_RESPONSE)
	public ResponseEntity<BaseResponse<SurveyResponse>> submitSurveyResponse(@Valid @RequestBody SubmitSurveyRequestDto dto){
		return ResponseEntity.ok(BaseResponse.<SurveyResponse>builder()
				                         .code(200)
				                         .success(true)
				                         .message("Yanıt başarıyla gönderildi.")
				                         .data(surveyResponseService.submitSurveyResponse(dto))
		                                     .build());
	}
	
	@GetMapping(GET_USER_RESPONSES+"/{userId}")
	public ResponseEntity<BaseResponse<List<SurveyResponse>>> getUserSurveyResponse(@PathVariable Long userId){
		return ResponseEntity.ok(BaseResponse.<List<SurveyResponse>>builder()
				                         .code(200)
				                         .success(true)
				                         .message("Kullanıcının anket yanıtları getirildi.")
				                         .data(surveyResponseService.getUserResponses(userId))
		                                     .build());
	}
	
	@GetMapping(GET_RESPONSE_DETAILS+"/{responseId}")
	public ResponseEntity<BaseResponse<SurveyResponseDetailDto>> getResponseWithDetails(@PathVariable String responseId){
		return ResponseEntity.ok(BaseResponse.<SurveyResponseDetailDto>builder()
				                         .code(200)
				                         .success(true)
				                         .message("Anket yanıtları detaylıca getirildi.")
				                         .data(surveyResponseService.getResponseWithDetails(responseId))
		                                     .build());
	}
}