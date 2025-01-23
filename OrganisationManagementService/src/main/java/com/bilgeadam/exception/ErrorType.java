package com.bilgeadam.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
    INTERNAL_SERVER_ERROR(500,"Sunucuda beklenmeyen bir hata oldu. Lütfen tekrar deneyiniz",HttpStatus.INTERNAL_SERVER_ERROR),
    VALIDATION_ERROR(400,"Girilen parametreler hatalıdır. Lütfen kontrol ederek tekrar deneyiniz.", HttpStatus.BAD_REQUEST),
    DEPARTMENT_NOT_FOUND(1001,"Departman bulunamadı",HttpStatus.BAD_REQUEST),
    EMPLOYEE_NOT_FOUND(2001,"Çalışan bulunamadı",HttpStatus.BAD_REQUEST),
    ;

    int code;
    String message;
    HttpStatus httpStatus;
}
