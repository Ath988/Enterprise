package com.bilgeadam.entity.enums;

public enum TicketCategory {
	TECHNICAL_SUPPORT("TEKNİK DESTEK"),
	BILLING("FATURALAMA"),
	PRODUCT_ISSUE("ÜRÜN SORUNU"),
	GENERAL_INQUIRY("GENEL SORU"),
	OTHER("DİĞER");
	
	private final String description;
	
	TicketCategory(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}