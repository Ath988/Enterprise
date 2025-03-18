package com.bilgeadam.enterprise.dto.response;

public record UserDetailForChatResponse(
		Long id,
		Long companyId,
		String name,
		String surname,
		String avatarUrl,
		Boolean isOnline

) {
}