package com.bilgeadam.controller;

import com.bilgeadam.dto.request.CreateSurveyRequestDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.dto.response.SurveyDetailDto;
import com.bilgeadam.entity.Survey;
import com.bilgeadam.service.SurveyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constant.Apis.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(SURVEY)
@CrossOrigin("*")
public class SurveyController {
	private final SurveyService surveyService;
	
	
	@PostMapping(CREATE_SURVEY)
	public ResponseEntity<BaseResponse<Boolean>> createSurvey(@RequestHeader("token") String token
			,@Valid @RequestBody CreateSurveyRequestDto createSurveyRequestDto) {
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
				                         .code(200)
				                         .message("Anket başarıyla kaydedildi.")
				                         .success(true)
				                         .data(surveyService.createSurvey(token,createSurveyRequestDto))
		                                     .build());
	}
	
	@GetMapping(GET_ACTIVE_SURVEYS)
	public ResponseEntity<BaseResponse<List<SurveyDetailDto>>> getActiveSurveys(@RequestHeader("token") String token) {
		return ResponseEntity.ok(BaseResponse.<List<SurveyDetailDto>>builder()
				                         .code(200)
				                         .message("Anket listesi getirildi")
				                         .success(true)
				                         .data(surveyService.getAllSurveys(token))
		                                     .build());
	}
	
	@GetMapping(GET_SURVEY_DETAILS+"/{surveyId}")
	public ResponseEntity<BaseResponse<SurveyDetailDto>> getSurveyWithDetails(@RequestHeader("token") String token,@PathVariable String surveyId) {
		return ResponseEntity.ok(BaseResponse.<SurveyDetailDto>builder()
				                         .code(200)
				                         .message("Anket detayları getirildi")
				                         .success(true)
				                         .data(surveyService.getSurveyDetail(token,surveyId))
		                                     .build());
	}
	
	@DeleteMapping(DELETE_SURVEY+"/{surveyId}")
	public ResponseEntity<BaseResponse<Boolean>> deleteSurvey(@RequestHeader("token") String token,@PathVariable String surveyId) {
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
				                         .code(200)
				                         .message("Anket başarıyla silindi.")
				                         .success(true)
				                         .data(surveyService.deleteSurvey(token,surveyId))
		                                     .build());
	}
	
	@PutMapping(UPDATE_SURVEY+"/{surveyId}")
	public ResponseEntity<BaseResponse<Boolean>> updateSurvey(@RequestHeader("token") String token,
	                                                          @PathVariable String surveyId,@RequestBody CreateSurveyRequestDto dto){
		return ResponseEntity.ok(BaseResponse.<Boolean>builder()
				                         .code(200)
				                         .message("Anket başarıyla güncellendi.")
				                         .success(true)
				                         .data(surveyService.updateSurvey(token,surveyId,dto))
		                                     .build());
	}
}