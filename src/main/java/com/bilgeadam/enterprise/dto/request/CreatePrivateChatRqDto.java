package com.bilgeadam.enterprise.dto.request;

import java.util.Set;

public record CreatePrivateChatRqDto(
		Set<String> userIds
) {
}