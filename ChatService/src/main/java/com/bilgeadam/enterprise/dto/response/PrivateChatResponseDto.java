package com.bilgeadam.enterprise.dto.response;

import com.bilgeadam.enterprise.entity.Message;
import com.bilgeadam.enterprise.view.MessageView;
import com.bilgeadam.enterprise.view.UserView;

import java.util.List;

public record PrivateChatResponseDto(
		String id,
		String recipientName,
		UserView recipient
) {
}