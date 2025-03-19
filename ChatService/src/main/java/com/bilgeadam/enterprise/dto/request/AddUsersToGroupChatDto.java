package com.bilgeadam.enterprise.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Set;

public record AddUsersToGroupChatDto(
		@NotBlank
		String chatId,
		List<Long> userIds
) {
}