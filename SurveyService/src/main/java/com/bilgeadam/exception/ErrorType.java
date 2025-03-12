package com.bilgeadam.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
    INTERNAL_SERVER_ERROR(500,"Sunucuda beklenmeyen bir hata oldu. Lütfen tekrar deneyiniz",HttpStatus.INTERNAL_SERVER_ERROR),
    VALIDATION_ERROR(400,"Girilen parametreler hatalıdır. Lütfen kontrol ederek tekrar deneyiniz.", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(501,"Geçersiz token bilgisi!", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(401,"Unauthorized",HttpStatus.UNAUTHORIZED),
    SURVEY_NOTFOUND(101,"Anket Bulunamadı!", HttpStatus.NOT_FOUND),
    RESPONSE_NOTFOUND(102,"Yanıt Bulunamadı!", HttpStatus.NOT_FOUND),
    COMPANY_NOTFOUND(103,"Şirket Bulunamadı!", HttpStatus.NOT_FOUND),
    SURVEY_ALREADY_SUBMITTED(104,"Anket zaten çözülmüş.", HttpStatus.BAD_REQUEST),
    SURVEY_CREATE_ERROR(105,"Anket oluşturulamıyor.", HttpStatus.BAD_REQUEST),
    SURVEY_SUBMIT_ERROR(106,"Anket gönderilemedi!" ,HttpStatus.BAD_REQUEST ),
    COMPANY_ID_NOT_FOUND(107,"Şirket ID'si bulunamadı!" , HttpStatus.NOT_FOUND );
   


    int code;
    String message;
    HttpStatus httpStatus;
}