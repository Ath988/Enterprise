package com.bilgeadam.enterprise.dto.response;

import com.bilgeadam.enterprise.entity.EMessageStatus;
import com.bilgeadam.enterprise.entity.User;
import com.bilgeadam.enterprise.view.UserView;

import java.time.LocalDateTime;

public record NewMessageResponseDto(
		String messageId,
		String content,
		UserView sender,
		LocalDateTime timeStamp,
		EMessageStatus messageStatus
		
) {
}