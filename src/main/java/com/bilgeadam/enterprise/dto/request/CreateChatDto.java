package com.bilgeadam.enterprise.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateChatDto(
		@NotBlank
		String chatCreatorId,
		String name,
		String description,
		@NotBlank
		List<String> chatUserIds
		
) {
}