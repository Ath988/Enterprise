package com.bilgeadam.dto.response;

import com.bilgeadam.entity.Survey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyDetailDto {
	private Survey survey;
	private List<QuestionDetailDto> questions;
}