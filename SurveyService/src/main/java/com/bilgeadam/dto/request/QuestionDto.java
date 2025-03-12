package com.bilgeadam.dto.request;

import com.bilgeadam.entity.enums.EQuestionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {
	private String questionText;
	
	@JsonProperty("questionType")
	@JsonDeserialize(as = EQuestionType.class)
	private EQuestionType questionType;
	
	@JsonProperty("options")
	private List<OptionDto> options;
}