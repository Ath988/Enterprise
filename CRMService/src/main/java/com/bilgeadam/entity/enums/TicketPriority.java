package com.bilgeadam.entity.enums;

public enum TicketPriority {
	LOW("Düşük"),
	MEDIUM("Orta"),
	HIGH("Yüksek"),
	CRITICAL("Kritik");
	
	private final String description;
	
	TicketPriority(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}