package com.bilgeadam.dto.request;

public record UpdateProfileRequestDto(
        String token,
        String firstname,
        String lastname,
        String email
) {
}
