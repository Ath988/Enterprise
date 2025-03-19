package com.bilgeadam.dto.response;

public record UserDetailsForChatResponse(
		Long id,
		Long companyId,
		String name,
		String surname,
		String avatarUrl,
		Boolean isOnline
) {
}