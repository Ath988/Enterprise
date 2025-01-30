package com.bilgeadam.enterprise.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record UpdateChatDetailsDto(
		@NotBlank
		String chatId,
		@NotBlank
        String name,
		@NotEmpty
        String description
) {
}