package com.bilgeadam.enterprise.dto.response;

import java.time.LocalDateTime;

public record GroupChatCreateResponseDto(
		String id,
		String name,
		String description,
		LocalDateTime createDate
) {
}