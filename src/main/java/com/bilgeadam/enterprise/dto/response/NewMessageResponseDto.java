package com.bilgeadam.enterprise.dto.response;

import com.bilgeadam.enterprise.entity.EMessageStatus;
import com.bilgeadam.enterprise.entity.User;

import java.time.LocalDateTime;

public record NewMessageResponseDto(
		String messageId,
		String content,
		User sender,
		LocalDateTime timeStamp,
		EMessageStatus messageStatus
		
) {
}