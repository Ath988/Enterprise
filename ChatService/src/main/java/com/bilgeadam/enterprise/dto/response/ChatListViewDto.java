package com.bilgeadam.enterprise.dto.response;


import com.bilgeadam.enterprise.entity.EChatType;

import java.time.LocalDateTime;
import java.util.Optional;

public record ChatListViewDto(
		String chatId,
		EChatType chatType,
		String chatName,
		LocalDateTime lastMessageDate,
		String lastMessage,
		String chatImage,
		Boolean isOnline
		
) {}