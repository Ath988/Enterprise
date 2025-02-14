package com.bilgeadam.enterprise.dto.response;

import com.bilgeadam.enterprise.view.MessageView;

import java.time.LocalDateTime;
import java.util.List;

public record GroupChatCreateResponseDto(
		String id,
		String name,
		String description,
		LocalDateTime createDate,
		String chatImageUrl
) {
}