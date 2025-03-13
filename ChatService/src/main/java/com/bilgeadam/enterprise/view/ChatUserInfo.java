package com.bilgeadam.enterprise.view;

import java.time.LocalDateTime;

public record ChatUserInfo(
		String chatId,
		LocalDateTime createdAt,
		String description,
		String name,
		Long userId
) {}