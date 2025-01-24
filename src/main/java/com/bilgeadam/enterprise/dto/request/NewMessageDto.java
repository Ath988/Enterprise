package com.bilgeadam.enterprise.dto.request;

public record NewMessageDto(
	String content,
	String chatId
) {
}