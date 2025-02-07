package com.bilgeadam.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum ErrorType {
    VALIDATION_ERROR(400,"Girilen parametreler hatalıdır. Lütfen kontrol ederek tekrar deneyin.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR(500,"Sunucuda beklenmeyen bir hata oldu. Lütfen tekrar deneyin",HttpStatus.INTERNAL_SERVER_ERROR),
    LOGIN_ERROR(1000, "e-posta yada sifre hatali!", HttpStatus.BAD_REQUEST),
    LOGIN_ERROR_EMAIL_VALIDATION(1010, "e-posta onayi yapmadan giris yapamazsiniz!", HttpStatus.BAD_REQUEST),
    REGISTER_ERROR(1001, "e-posta adresi sisteme kayitlidir! Lutfen bir baska e-posta adresi yada giris yapmayi deniyiniz!", HttpStatus.CONFLICT),
    INVALID_PASSWORD(1002, "sifreler uyusmuyor!", HttpStatus.BAD_REQUEST),
    NOTFOUND_USER(1003, "Kullanici Bulunamadi", HttpStatus.NOT_FOUND),
    NOTFOUND_USER_AUTH(1004, "Kullanici onay kodu gecersiz!", HttpStatus.NOT_FOUND),
    ACCOUNT_NOT_FOUND(1005, "Hesap Bulunamadı!", HttpStatus.NOT_FOUND),
    INVALID_TOKEN(1006,"Girdiğiniz token hatalıdır. Lütfen değiştirerek tekrar deneyiniz.",HttpStatus.UNAUTHORIZED),
    TRANSACTION_NOT_FOUND(1007,"Gelir-Gider Bulunamadı",HttpStatus.UNAUTHORIZED);

    int code;
    String message;
    HttpStatus httpStatus;
}
