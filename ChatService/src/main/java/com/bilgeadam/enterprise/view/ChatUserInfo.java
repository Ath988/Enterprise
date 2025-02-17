package com.bilgeadam.enterprise.view;

import java.time.LocalDateTime;

public record ChatUserInfo(
		String chatId,
		LocalDateTime createdAt,
		String description,
		String name,
		String userId,
		String userName,
		String userSurname,
		Boolean isOnline,
		String profilePicture
) { }