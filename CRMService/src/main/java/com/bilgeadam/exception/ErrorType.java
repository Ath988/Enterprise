package com.bilgeadam.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
	INTERNAL_SERVER_ERROR(500, "Sunucuda beklenmeyen bir hata oldu. Lütfen tekrar deneyin", HttpStatus.INTERNAL_SERVER_ERROR),
	VALIDATION_ERROR(400,"girilen parametreler hatalıdır. Lütfen kontrol ederek tekrar deneyin.", HttpStatus.BAD_REQUEST),
	CUSTOMER_NOT_FOUND(100,"Customer not found", HttpStatus.NOT_FOUND),
	SALES_OPPORTUNITY_NOT_FOUND(200,"Sales opportunity not found", HttpStatus.NOT_FOUND),
	CAMPAIGN_NOT_FOUND(300,"Campaign not found", HttpStatus.NOT_FOUND),
	SUPPORT_TICKET_NOT_FOUND(600,"Support ticket not found", HttpStatus.NOT_FOUND),;
	
	int code;
	String message;
	HttpStatus httpStatus;
}