package com.bilgeadam.enterprise.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewMessageDto(
		@NotBlank(message = "Message content cannot be blank")
		@Size(max = 100, message = "Message content cannot exceed 100 characters")
		String content,
		
		@NotBlank(message = "Chat ID cannot be blank")
		String chatId
) {
}