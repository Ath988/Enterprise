package com.bilgeadam.dto.request;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
public record AddAnswerRequestDto(
		Long faqId,
		String answer
) {
}