package com.bilgeadam.views;

import com.bilgeadam.entity.enums.OfferStatus;

public record VwOffer(
		String title,
		OfferStatus status,
		String customerName,
		String email
) {
}