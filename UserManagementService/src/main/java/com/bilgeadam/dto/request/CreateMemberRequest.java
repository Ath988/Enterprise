package com.bilgeadam.dto.request;

import jakarta.validation.constraints.*;

public record CreateMemberRequest(
        Long authId,
        String firstName,
        String lastName,
        String email
) {
}
