package com.bilgeadam.dto.request;

public record UpdateSupportTicketRequestDto(
		String subject,
		String description
) {
}