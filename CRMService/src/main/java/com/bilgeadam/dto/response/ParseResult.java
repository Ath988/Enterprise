package com.bilgeadam.dto.response;

import com.bilgeadam.entity.Customer;

public record ParseResult(
		Customer customer,
		String errorMessage
) {
}