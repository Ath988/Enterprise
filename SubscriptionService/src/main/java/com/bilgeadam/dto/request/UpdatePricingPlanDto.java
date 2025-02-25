package com.bilgeadam.dto.request;

import java.util.List;

public record UpdatePricingPlanDto(
		 Long id,
		 Double price,
		 String title,
		 List<String>features
) {
}