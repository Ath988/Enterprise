package com.bilgeadam.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddSalesOpportunityRequestDto(
		@NotNull
		@Size(min = 3, max = 100)
		String title,
		String description,
		Double estimatedValue,
		Long customerId,
		Long campaignId
) {
}