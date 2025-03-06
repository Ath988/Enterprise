package com.bilgeadam.ticketservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum ErrorType {
    VALIDATION_ERROR(9001,"girilen parametreler hatalıdır. Lütfen kontrol ederek tekrar deneyin.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR(9000,"Sunucuda beklenmeyen bir hata oldu. Lütfen tekrar deneyin",HttpStatus.INTERNAL_SERVER_ERROR),


    TICKET_NOT_FOUND(9002, "verilen id'ye sahip bir ticket bulunamadı", HttpStatus.NOT_FOUND),
    INVALID_TOKEN(9003, "verilen token geçersiz" , HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(9004, "kullanıcının rolü bu işlem için yeterli değil" , HttpStatus.BAD_REQUEST),
    INVALID_RESPONSE_TICKET_STATUS(9005,"yanıtlanan ticket pending status'ünde olamaz" , HttpStatus.BAD_REQUEST ),
    INVALID_ANSWERED_RESPONSE_TEXT(9006, "olumlu yanıtlanan ticket'larda response metni bulunmalıdır",HttpStatus.BAD_REQUEST ),
    USER_TICKET_CONFLICT(9007,"kullanıcı bu ticket'ın sahibi değil" , HttpStatus.CONFLICT);
    int code;
    String message;
    HttpStatus httpStatus;
}
