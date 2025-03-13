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
	INVALID_CUSTOMER_NAME(101,"The customer name is invalid.", HttpStatus.BAD_REQUEST),
	CUSTOMER_DELETE_LIST_EMPTY(104, "The customer list to be deleted cannot be empty.", HttpStatus.BAD_REQUEST),
	CUSTOMER_IMPORT_EMPTY(105,"The customer import file is empty.", HttpStatus.BAD_REQUEST),
	EMAIL_ALREADY_EXISTS(106,"This email address is already registered.", HttpStatus.CONFLICT),
	PHONE_ALREADY_EXISTS(107,"This phone number is already registered.", HttpStatus.CONFLICT),
	OFFER_NOT_FOUND(200,"Offer not found", HttpStatus.NOT_FOUND),
	TICKET_NOT_FOUND(300,"Ticket not found", HttpStatus.NOT_FOUND),
	TICKET_UPDATE_FAILED(302,"Ticket update failed", HttpStatus.BAD_REQUEST),
	TICKET_CREATION_FAILED(304,"Ticket creation failed", HttpStatus.CONFLICT),
	EXCEL_READ_ERROR(600,"An error occurred while reading the Excel file.", HttpStatus.INTERNAL_SERVER_ERROR),
	INVALID_EXCEL_HEADERS(602, "Excel başlıkları geçersiz. Lütfen sistemin sağladığı şablonu kullanın.", HttpStatus.BAD_REQUEST),
	EXCEL_TEMPLATE_ERROR(603, "Excel şablonu oluşturulurken hata oluştu.", HttpStatus.INTERNAL_SERVER_ERROR),
	PDF_GENERATION_FAILED(601,"An error occurred while reading the Pdf file.",HttpStatus.INTERNAL_SERVER_ERROR),
	PERFORMER_NOT_FOUND(704, "Performer not found.", HttpStatus.NOT_FOUND),
	TICKET_INACTIVE_CANNOT_UPDATE(403, "Inactive tickets cannot be updated.", HttpStatus.FORBIDDEN);
	
	int code;
	String message;
	HttpStatus httpStatus;
}