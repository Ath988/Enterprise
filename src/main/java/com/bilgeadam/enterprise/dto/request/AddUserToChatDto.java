package com.bilgeadam.enterprise.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record AddUserToChatDto(
		@NotBlank
		String chatId,
		@NotBlank
		Set<String> users
) {
}