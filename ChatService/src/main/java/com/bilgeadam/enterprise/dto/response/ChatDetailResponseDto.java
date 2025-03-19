package com.bilgeadam.enterprise.dto.response;

import com.bilgeadam.enterprise.entity.Chat;
import com.bilgeadam.enterprise.entity.EChatType;
import com.bilgeadam.enterprise.entity.Message;
import com.bilgeadam.enterprise.view.MessageView;
import com.bilgeadam.enterprise.view.UserView;

import java.util.List;

public record ChatDetailResponseDto(
		String chatId,
		String name,
		String description,
		List<UserView> participants,
		List<MessageView> messages,
		String chatImageUrl,
		Long currentUserId,
		EChatType chatType
) {
}