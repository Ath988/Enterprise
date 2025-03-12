package com.bilgeadam.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AddOfferRequestDto(
		@NotNull
		@Size(min = 3, max = 100)
		String title,
		
		@NotNull
		@Size(min = 3, max = 500)
		String description,
		
		@NotNull
		LocalDate expirationDate,
		
		@NotNull
		LocalDate createdAt,
		
		Long customerId
) {
}