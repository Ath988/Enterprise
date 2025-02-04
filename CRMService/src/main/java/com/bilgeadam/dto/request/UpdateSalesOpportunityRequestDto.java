package com.bilgeadam.dto.request;

public record UpdateSalesOpportunityRequestDto(
		String description,
		Double estimatedValue,
		Long customerId,
		Long campaignId
) {
}