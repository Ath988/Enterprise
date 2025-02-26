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
    DEPARTMENT_NOT_FOUND(1001,"Departman bulunamadı",HttpStatus.BAD_REQUEST),
    EMPLOYEE_NOT_FOUND(2001,"Çalışan bulunamadı",HttpStatus.BAD_REQUEST),
    MANAGER_NOT_FOUND(2002,"Yönetici bulunamadı",HttpStatus.BAD_REQUEST),
    POSITION_NOT_FOUND(3001,"Pozisyon bulunamadı",HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(9001,"Geçersiz token bilgisi.",HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(9002,"Kullanıcı adı ya da şifre bilgisi hatalı.",HttpStatus.BAD_REQUEST),
    NOT_FOUND_ANNOUNCEMENT(4003,"Duyuru bulunamadı.",HttpStatus.BAD_REQUEST),
    CANNOT_DELETE_ANNOUNCEMENT(4004,"Duyuruyu silme yetkiniz bulunmamaktadır..",HttpStatus.BAD_REQUEST),
    SHIFT_NOT_FOUND(5000,"Vardiya Bulunamadı",HttpStatus.NOT_FOUND),
    ;

    int code;
    String message;
    HttpStatus httpStatus;
}