package com.bilgeadam.entity.enums;

public enum TicketStatus {
	NEW("Yeni"),
	IN_PROGRESS("Sürüyor"),
	WAITING("Beklemede"),
	RESOLVED("Çözüldü"),
	CLOSED("Kapandı");
	
	private final String description;
	
	TicketStatus(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}