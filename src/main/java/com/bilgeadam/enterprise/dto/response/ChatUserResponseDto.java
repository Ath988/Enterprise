package com.bilgeadam.enterprise.dto.response;

public record ChatUserResponseDto(
		String id,
		String name,
		String surname,
		String email,
		Boolean isOnline
) {
}