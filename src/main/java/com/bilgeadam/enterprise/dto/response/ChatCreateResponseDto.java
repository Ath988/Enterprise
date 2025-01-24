package com.bilgeadam.enterprise.dto.response;

import com.bilgeadam.enterprise.entity.Message;

import java.time.LocalDateTime;
import java.util.List;

public record ChatCreateResponseDto(
		String id,
		String name,
		String description,
		LocalDateTime createDate
) {
}