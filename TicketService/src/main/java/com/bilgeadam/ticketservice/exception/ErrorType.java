package com.bilgeadam.ticketservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum ErrorType {
    VALIDATION_ERROR(9001,"girilen parametreler hatalıdır. Lütfen kontrol ederek tekrar deneyin.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR(9000,"Sunucuda beklenmeyen bir hata oldu. Lütfen tekrar deneyin",HttpStatus.INTERNAL_SERVER_ERROR),


    NO_ACTIVE_SUBSCRIPTION(9002, "kullanıcının aktif bir aboneliği bulunamadı.", HttpStatus.NOT_FOUND),
    SAME_SUBSCRIPTION_PLAN(9003, "kullanıcı zaten sahip olduğu plana upgrade/ downgrade yapmaya çalışıyor", HttpStatus.BAD_REQUEST),
    ALREADY_CANCELLED(9004, "kullanıcının aboneliği zaten iptal edilmiş", HttpStatus.BAD_REQUEST),
    NO_CURRENT_SUBSCRIPTION(9005, "kullanıcının mevcut bir aboneliği bulunmamakta", HttpStatus.NOT_FOUND),
    INVALID_TOKEN(9004, "verilen token geçersiz" , HttpStatus.BAD_REQUEST);

    int code;
    String message;
    HttpStatus httpStatus;
}
