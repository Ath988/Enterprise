package com.bilgeadam.entity.enums;

public enum TicketPriority {
	LOW("DÜŞÜK"),
	MEDIUM("ORTA"),
	HIGH("YÜKSEK"),
	CRITICAL("KRİTİK");
	
	private final String description;
	
	TicketPriority(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}