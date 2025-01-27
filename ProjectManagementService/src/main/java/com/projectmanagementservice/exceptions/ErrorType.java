package com.projectmanagementservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType
{
    PROJECT_NOT_FOUND(9000, "Proje Bulunamadi", HttpStatus.BAD_REQUEST),


    VALIDATION_ERROR(400, "girilen parametreler hatalıdır. Lütfen kontrol ederek tekrar deneyin.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR(500, "Sunucuda beklenmeyen bir hata oldu. Lütfen tekrar deneyin", HttpStatus.INTERNAL_SERVER_ERROR),
    LOGIN_ERROR(1000, "e-posta yada sifre hatali!", HttpStatus.BAD_REQUEST),
    REGISTER_ERROR(1001, "e-posta adresi sisteme kayitlidir! Lutfen bir baska e-posta adresi yada giris yapmayi deniyiniz!", HttpStatus.CONFLICT),
    INVALID_PASSWORD(1002, "sifreler uyusmuyor!", HttpStatus.BAD_REQUEST),
    NOTFOUND_USER(1003, "Kullanici Bulunamadi", HttpStatus.NOT_FOUND),
    NOTFOUND_USER_AUTH(1004, "Kullanici onay kodu gecersiz!", HttpStatus.NOT_FOUND),
    TASK_NOT_FOUND(1005, "Task Bulunamadi", HttpStatus.BAD_REQUEST);

    int code;
    String message;
    HttpStatus httpStatus;
}
