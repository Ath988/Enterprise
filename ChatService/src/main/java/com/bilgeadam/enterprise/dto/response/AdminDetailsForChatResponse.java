package com.bilgeadam.enterprise.dto.response;

public record AdminDetailsForChatResponse(
        Long id,
        Long companyId,
        String firstName,
        String lastName,
        String avatarUrl,
        Boolean isOnline
) {
}
