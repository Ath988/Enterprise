package com.bilgeadam.enterprise.view;

public record UserView(
		String id,
		String name,
		String surname,
		Boolean isOnline,
		String profilePicture
) {
}