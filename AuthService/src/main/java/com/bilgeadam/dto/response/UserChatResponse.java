package com.bilgeadam.dto.response;

//ChatService'e dönen response
public record UserChatResponse(
		Long id,
		String name,
		String surname,
		Boolean isOnline,
		String profilePicture
) {
}