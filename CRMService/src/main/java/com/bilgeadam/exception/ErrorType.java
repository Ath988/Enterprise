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
	CUSTOMER_DELETE_LIST_EMPTY(104, "The customer list to be deleted cannot be empty.", HttpStatus.BAD_REQUEST),
	CUSTOMER_IMPORT_EMPTY(105,"The customer import file is empty.", HttpStatus.BAD_REQUEST),
	OFFER_NOT_FOUND(200,"Offer not found", HttpStatus.NOT_FOUND),
	TICKET_NOT_FOUND(300,"Ticket not found", HttpStatus.NOT_FOUND),
	TICKET_UPDATE_FAILED(302,"Ticket update failed", HttpStatus.BAD_REQUEST),
	TICKET_CREATION_FAILED(304,"Ticket creation failed", HttpStatus.CONFLICT);
	
	int code;
	String message;
	HttpStatus httpStatus;
}