package com.bilgeadam.enterprise.dto.response;

import com.bilgeadam.enterprise.entity.Chat;
import com.bilgeadam.enterprise.entity.EChatType;
import com.bilgeadam.enterprise.entity.Message;
import com.bilgeadam.enterprise.entity.User;

import java.util.List;
import java.util.Set;

public record ChatResponseDto(
		List<Chat> usersChat
) {
}