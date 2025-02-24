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
	public ResponseEntity<BaseResponse<Boolean>> submitSurveyResponse(@RequestHeader("token") String token,@Valid @RequestBody SubmitSurveyRequestDto dto){
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
				                         .code(200)
				                         .success(true)
				                         .message("Yanıt başarıyla gönderildi.")
				                         .data(surveyResponseService.submitSurveyResponse(token,dto))
		                                     .build());
	}
	
	@GetMapping(GET_USER_RESPONSES)
	public ResponseEntity<BaseResponse<List<SurveyResponseDetailDto>>> getUserSurveyResponse(@RequestHeader("token") String token){
		return ResponseEntity.ok(BaseResponse.<List<SurveyResponseDetailDto>>builder()
				                         .code(200)
				                         .success(true)
				                         .message("Kullanıcının anket yanıtları getirildi.")
				                         .data(surveyResponseService.getUserSurveyResponses(token))
		                                     .build());
	}
	
	@GetMapping(GET_RESPONSE_DETAILS+"/{surveyId}")
	public ResponseEntity<BaseResponse<List<SurveyResponseDetailDto>>> getResponseWithDetails(@RequestHeader("token") String token,@PathVariable String surveyId){
		return ResponseEntity.ok(BaseResponse.<List<SurveyResponseDetailDto>>builder()
				                         .code(200)
				                         .success(true)
				                         .message("Anket yanıtları detaylıca getirildi.")
				                         .data(surveyResponseService.getSurveyResponses(token,surveyId))
		                                     .build());
	}
}