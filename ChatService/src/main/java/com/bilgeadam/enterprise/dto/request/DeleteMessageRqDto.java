package com.bilgeadam.enterprise.dto.request;

import jakarta.validation.constraints.NotBlank;

public record DeleteMessageRqDto(
		@NotBlank
		String messageId
) {
}