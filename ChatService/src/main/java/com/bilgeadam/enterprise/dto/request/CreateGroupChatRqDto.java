package com.bilgeadam.enterprise.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record CreateGroupChatRqDto(
		String name,
		String description,
		@Size(min = 2, message = "A group chat must have at least 2 participants")
		Set<String> userIds,
		String chatImageUrl
) {
}