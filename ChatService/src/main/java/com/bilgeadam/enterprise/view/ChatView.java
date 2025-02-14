package com.bilgeadam.enterprise.view;

import java.time.LocalDateTime;
import java.util.List;

public record ChatView(
		List<UserView> chatUsers,
		LocalDateTime createdAt,
		String description,
		String name
) {
}