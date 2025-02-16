package com.bilgeadam.views;

import com.bilgeadam.entity.enums.ActivityType;

import java.time.LocalDateTime;

public record VwTicketActivity(
		ActivityType type,
		LocalDateTime timestamp,
		String performedByName,
		boolean isStaff,
		String content
) {
}