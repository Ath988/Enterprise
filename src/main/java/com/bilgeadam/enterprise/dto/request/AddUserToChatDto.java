package com.bilgeadam.enterprise.dto.request;

import java.util.List;
import java.util.Set;

public record AddUserToChatDto(
		String id,
		Set<String> users
) {
}