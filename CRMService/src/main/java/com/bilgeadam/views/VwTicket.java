package com.bilgeadam.views;

import com.bilgeadam.entity.enums.TicketPriority;
import com.bilgeadam.entity.enums.TicketStatus;

import java.time.LocalDateTime;

public record VwTicket(
		String subject,
		TicketStatus status,
		TicketPriority priority,
		LocalDateTime createdAt,
		String customerName,
		String customerEmail
) {
}