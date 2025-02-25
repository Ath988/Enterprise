package com.bilgeadam.dto.response;

import com.bilgeadam.entity.Answer;
import com.bilgeadam.entity.SurveyResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyResponseDetailDto {
	private SurveyResponse surveyResponse;
	private List<Answer> answers;
}