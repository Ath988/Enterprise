package com.bilgeadam.entity.enums;

public enum ActivityType {
	CREATION("Oluşturuldu"),
	CONTACT("Bağlantı"),
	STATUS_CHANGE("Durumda Değişiklik"),
	NOTE("Not"),
	RESOLUTION("Çözülme");
	
	private final String description;
	
	ActivityType(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}