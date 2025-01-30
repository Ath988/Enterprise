package com.bilgeadam.enterprise.dto.response;


import java.time.LocalDateTime;

public record ChatListViewDto(
		String chatId,
		String chatName,
		LocalDateTime createDate,
		String lastMessage
) {}