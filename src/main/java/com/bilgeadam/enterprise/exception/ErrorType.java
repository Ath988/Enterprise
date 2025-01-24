package com.bilgeadam.enterprise.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
	USER_NOT_FOUND(1001,"User not found!",HttpStatus.NOT_FOUND),
	USER_NOT_AUTHORIZED(1002,"User not authorized!",HttpStatus.UNAUTHORIZED),
	CHAT_NOT_FOUND(2001,"Chat not found!",HttpStatus.NOT_FOUND);
	int code;
	String message;
	HttpStatus httpStatus;
}