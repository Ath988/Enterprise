package com.bilgeadam.dto.request;

public record CreateCompanyManagerRequest(
        Long authId,
        String email
) {
}
