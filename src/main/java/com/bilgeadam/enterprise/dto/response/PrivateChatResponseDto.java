package com.bilgeadam.enterprise.dto.response;

import com.bilgeadam.enterprise.entity.Message;

import java.util.List;

public record PrivateChatResponseDto(
		String id,
		String recipientName,
		List<Message> lastTenMessage
) {
}