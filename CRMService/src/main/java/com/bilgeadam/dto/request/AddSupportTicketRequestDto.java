package com.bilgeadam.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddSupportTicketRequestDto(
		@NotNull
		@Size(min = 3, max = 100)
		String subject,
		@NotNull
		String description,
		@NotNull
		Long customerId
) {
}