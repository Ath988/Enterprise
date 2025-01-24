package com.bilgeadam.enterprise.dto.response;

import com.bilgeadam.enterprise.entity.EMessageStatus;

import java.time.LocalDateTime;

public record NewMessageResponseDto(
		String id,
		LocalDateTime timeStamp,
		EMessageStatus messageStatus
		
) {
}