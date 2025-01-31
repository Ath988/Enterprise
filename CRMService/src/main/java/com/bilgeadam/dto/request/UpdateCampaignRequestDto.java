package com.bilgeadam.dto.request;

import java.time.LocalDateTime;

public record UpdateCampaignRequestDto(
		String name,
		String description,
		Double budget,
		LocalDateTime startDate,
		LocalDateTime endDate,
		Long customerId
) {
}