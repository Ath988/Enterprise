package com.bilgeadam.dto.request;

public record UpdateAnswerDto(
		Long id,
		Long faqId,
		String answer
) {
}