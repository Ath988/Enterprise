package com.bilgeadam.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
    INTERNAL_SERVER_ERROR(500,"Sunucuda beklenmeyen bir hata oldu. Lütfen tekrar deneyiniz",HttpStatus.INTERNAL_SERVER_ERROR),
    VALIDATION_ERROR(400,"Girilen parametreler hatalıdır. Lütfen kontrol ederek tekrar deneyiniz.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(401,"Unauthorized",HttpStatus.UNAUTHORIZED),
    EMPLOYEE_RECORD_NOT_FOUND(1001,"Çalışan kaydı bulunamadı",HttpStatus.BAD_REQUEST),
    PERFORMANCE_NOT_FOUND(2001,"Performans kaydı bulunamadı",HttpStatus.BAD_REQUEST),
    PAYROLL_NOT_FOUND(3001,"Maaş bordrosu bulunamadı",HttpStatus.BAD_REQUEST),
    WORKING_HOURS_NOT_FOUND(4001,"Çalışma saati kaydı bulunamadı",HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(9001,"Geçersiz token bilgisi.",HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(9002,"Kullanıcı adı ya da şifre bilgisi hatalı.",HttpStatus.BAD_REQUEST),

    ;

    int code;
    String message;
    HttpStatus httpStatus;
}
