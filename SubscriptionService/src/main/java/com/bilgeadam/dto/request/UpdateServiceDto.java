package com.bilgeadam.dto.request;

import java.util.List;

public record UpdateServiceDto(
		Long id,
		String title,
		List<String> descriptions
) {
}