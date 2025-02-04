package com.bilgeadam.enterprise.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorMessage> handleRuntimeException(RuntimeException exception) {
		log.error("Global exception: {}", exception.getMessage(), exception);
		return createResponseEntity(ErrorType.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, null);
	}
	
	@ExceptionHandler(EnterpriseException.class)
	@ResponseBody
	public ResponseEntity<ErrorMessage> handleEnterpriseException(EnterpriseException exception) {
		log.error("EnterpriseException occurred: {}", exception.getMessage(), exception);
		return createResponseEntity(exception.getErrorType(), exception.getErrorType().getHttpStatus(), null);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public ResponseEntity<ErrorMessage> handleValidationException(MethodArgumentNotValidException exception) {
		List<String> fieldErrors = new ArrayList<>();
		exception.getBindingResult().getFieldErrors().forEach(f ->
				                                                      fieldErrors.add("Field: " + f.getField() + " - Error: " + f.getDefaultMessage())
		);
		return createResponseEntity(ErrorType.VALIDATION_ERROR, HttpStatus.BAD_REQUEST, fieldErrors);
	}
	
	private ResponseEntity<ErrorMessage> createResponseEntity(ErrorType errorType, HttpStatus httpStatus, List<String> fields) {
		log.error("Error occurred: {}", errorType.getMessageTemplate());
		if (fields != null) log.error("Fields: {}", fields);
		
		ErrorMessage errorMessage = new ErrorMessage();
		errorMessage.setCode(errorType.getCode());
		errorMessage.setMesssage(errorType.getMessageTemplate());
		errorMessage.setSuccess(false);
		errorMessage.setFields(fields);
		
		return new ResponseEntity<>(errorMessage, httpStatus);
	}
}