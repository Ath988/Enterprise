package com.bilgeadam.exception;

import lombok.Getter;

@Getter
public class CRMServiceException extends RuntimeException {
	private ErrorType errorType;
	public CRMServiceException(ErrorType errorType) {
		super(errorType.getMessage());
		this.errorType = errorType;
	}
}