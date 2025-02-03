package com.bilgeadam.dto.request;

public record UpdateCustomerRequestDto(
		String email,
		String phone
) {
}