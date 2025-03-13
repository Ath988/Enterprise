package com.bilgeadam.entity.enums;

public enum TicketStatus {
	NEW("YENİ"),
	IN_PROGRESS("SÜRÜYOR"),
	WAITING("BEKLEMEDE"),
	RESOLVED("ÇÖZÜLDÜ"),
	CLOSED("KAPANDI");
	
	private final String description;
	
	TicketStatus(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}