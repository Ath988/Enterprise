package com.bilgeadam.enterprise.view;

import com.bilgeadam.enterprise.entity.Message;

import java.time.LocalDateTime;

public record MessageView(
		String id,
		String content,
		String senderId,
		String senderName,
		String senderSurname,
		LocalDateTime timeStamp,
		String chatId
) {
}