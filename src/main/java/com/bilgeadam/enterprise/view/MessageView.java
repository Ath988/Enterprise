package com.bilgeadam.enterprise.view;

import com.bilgeadam.enterprise.entity.Message;

public record MessageView(
		String id,
		String content,
		String senderId,
		String senderName,
		String senderSurname
) {
	public static MessageView fromEntity(Message message) {
		return new MessageView(
				message.getId(),
				message.getContent(),
				message.getSender().getId(),
				message.getSender().getName(),
				message.getSender().getSurname()
		);
	}
}