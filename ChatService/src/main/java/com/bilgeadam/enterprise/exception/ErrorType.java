package com.bilgeadam.enterprise.exception;

import lombok.Getter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
	USER_NOT_FOUND(1001, "User not found!", HttpStatus.NOT_FOUND),
	USER_NOT_AUTHORIZED(1002, "User not authorized!", HttpStatus.UNAUTHORIZED),
	CHAT_NOT_FOUND(2001, "Chat not found!", HttpStatus.NOT_FOUND),
	CHAT_ALREADY_EXISTS(2002, "Chat already exists!", HttpStatus.BAD_REQUEST),
	INVALID_CHAT_PARTICIPANTS(2003, "Invalid chat participant!", HttpStatus.BAD_REQUEST),
	USER_NOT_PARTICIPANT(2004, "User not participant!", HttpStatus.BAD_REQUEST),
	USER_ALREADY_IN_CHAT(2005, "User(s) already in chat: %s", HttpStatus.BAD_REQUEST),
	MESSAGE_NOT_FOUND(3001,"Message not found!",HttpStatus.NOT_FOUND),
	INVALID_CHAT_TYPE(2006,"Invalid chat type", HttpStatus.BAD_REQUEST),
	INTERNAL_SERVER_ERROR(5000, "An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR),
	VALIDATION_ERROR(4000, "Validation error occurred.", HttpStatus.BAD_REQUEST),
	SUPPORT_NOT_FOUND(6000,"No support personnel is registered in the system" ,HttpStatus.NOT_FOUND ),
	EXTERNAL_SERVICE_ERROR(7000,"External Service Error!",HttpStatus.SERVICE_UNAVAILABLE),
	DATABASE_ERROR(8000,"Database Error!",HttpStatus.BAD_REQUEST),
	UNEXPECTED_ERROR(9000,"An unexpected error occurred!",HttpStatus.BAD_REQUEST);
	
	
	private final int code;
	private final String messageTemplate;
	private final HttpStatus httpStatus;
	
	/**
	 * Dinamik mesajlarla hata mesajını oluşturur.
	 * @param args Dinamik mesaj için argümanlar
	 * @return Formatlanmış hata mesajı
	 */
	public String getFormattedMessage(Object... args) {
		return String.format(this.messageTemplate, args);
	}
}