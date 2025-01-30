package com.bilgeadam.enterprise.view;

import com.bilgeadam.enterprise.entity.Message;

public record MessageView(
		String id,
		String content,
		String senderId,
		String senderName,
		String senderSurname
) {
}