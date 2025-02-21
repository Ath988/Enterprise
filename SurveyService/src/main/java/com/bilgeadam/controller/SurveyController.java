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
	public ResponseEntity<BaseResponse<Survey>> createSurvey(@Valid @RequestBody CreateSurveyRequestDto createSurveyRequestDto) {
		return ResponseEntity.ok(BaseResponse.<Survey>builder()
				                         .code(200)
				                         .message("Anket başarıyla kaydedildi.")
				                         .success(true)
				                         .data(surveyService.createSurvey(createSurveyRequestDto))
		                                     .build());
	}
	
	@GetMapping(GET_ACTIVE_SURVEYS)
	public ResponseEntity<BaseResponse<List<Survey>>> getActiveSurveys() {
		return ResponseEntity.ok(BaseResponse.<List<Survey>>builder()
				                         .code(200)
				                         .message("Anket listesi getirildi")
				                         .success(true)
				                         .data(surveyService.getActiveSurveys())
		                                     .build());
	}
	
	@GetMapping(GET_SURVEY_DETAILS+"/{surveyId}")
	public ResponseEntity<BaseResponse<SurveyDetailDto>> getSurveyWithDetails(@PathVariable String surveyId) {
		return ResponseEntity.ok(BaseResponse.<SurveyDetailDto>builder()
				                         .code(200)
				                         .message("Anket detayları getirildi")
				                         .success(true)
				                         .data(surveyService.getSurveyWithDetails(surveyId))
		                                     .build());
	}
}