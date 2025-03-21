package com.bilgeadam.enterprise.view;

public record UserView(
		Long id,
		String name,
		String surname,
		Boolean isOnline,
		String profilePicture
) {
}