package com.bilgeadam.entity.enums;

public enum ActivityType {
	CREATION("OLUŞTURULDU"),
	CONTACT("BAĞLANTI KURULDU"),
	STATUS_CHANGE("DURUMDA DEĞİŞİKLİK"),
	NOTE("NOT"),
	RESOLUTION("ÇÖZÜLDÜ");
	
	private final String description;
	
	ActivityType(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}