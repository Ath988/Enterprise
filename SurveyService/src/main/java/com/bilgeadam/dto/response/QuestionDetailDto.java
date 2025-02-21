package com.bilgeadam.dto.response;

import com.bilgeadam.entity.Option;
import com.bilgeadam.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDetailDto {
	private Question question;
	private List<Option> options;
}