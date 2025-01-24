package com.bilgeadam.enterprise.exception;

public class EnterpriseException extends RuntimeException {
	private ErrorType errorType;
	public EnterpriseException(ErrorType errorType){
		super(errorType.getMessage());
		this.errorType = errorType;
	}
}