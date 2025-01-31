package com.bilgeadam.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record AddCampaignRequestDto(
		@NotNull
		@Size(min = 3, max = 100)
		String name,
		String description,
		Double budget,
		LocalDateTime startDate,
		LocalDateTime endDate
) {
}