package com.bilgeadam.views;

import java.util.List;

public record TicketDetailView(
		VwTicket ticket,
		List<VwTicketActivity> activities
) {
}