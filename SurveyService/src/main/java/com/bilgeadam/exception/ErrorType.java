package com.bilgeadam.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
    INTERNAL_SERVER_ERROR(500,"Sunucuda beklenmeyen bir hata oldu. Lütfen tekrar deneyiniz",HttpStatus.INTERNAL_SERVER_ERROR),
    VALIDATION_ERROR(400,"Girilen parametreler hatalıdır. Lütfen kontrol ederek tekrar deneyiniz.", HttpStatus.BAD_REQUEST),
    SURVEY_NOTFOUND(101,"Anket Bulunamadı!", HttpStatus.NOT_FOUND),
    RESPONSE_NOTFOUND(102,"Yanıt Bulunamadı!", HttpStatus.NOT_FOUND);
   


    int code;
    String message;
    HttpStatus httpStatus;
}