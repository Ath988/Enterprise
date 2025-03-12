package com.bilgeadam.dto.request;

public record CreateMemberRequest(
        Long authId,
        String firstName,
        String lastName,
        String email
) {
}
