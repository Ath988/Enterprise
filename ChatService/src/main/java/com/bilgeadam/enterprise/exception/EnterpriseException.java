package com.bilgeadam.enterprise.exception;

import lombok.Getter;

@Getter
public class EnterpriseException extends RuntimeException {
	private final ErrorType errorType;
	
	public EnterpriseException(ErrorType errorType) {
		super(errorType.getMessageTemplate());
		this.errorType = errorType;
	}
	
	public EnterpriseException(ErrorType errorType, String customMessage) {
		super(customMessage);
		this.errorType = errorType;
	}
}