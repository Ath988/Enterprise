package com.bilgeadam.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
    INTERNAL_SERVER_ERROR(500,"Sunucuda beklenmeyen bir hata oldu. Lütfen tekrar deneyiniz",HttpStatus.INTERNAL_SERVER_ERROR),
    VALIDATION_ERROR(400,"Girilen parametreler hatalıdır. Lütfen kontrol ederek tekrar deneyiniz.", HttpStatus.BAD_REQUEST),
    FILE_NOT_FOUND(101,"Dosya bulunamadı. Lütfen başka bir dosya ismi giriniz.",HttpStatus.NOT_FOUND),
    FILE_ALREADY_EXIST(102,"Bu isimde bir dosya zaten var. Lütfen başka bir dosya ismi giriniz.",HttpStatus.BAD_REQUEST),

    FOLDER_NOT_FOUND(201,"Klasor bulunamadı!",HttpStatus.NOT_FOUND),
    FOLDER_ALREADY_EXIST(202,"Bu isimde bir klasor zaten var. Lütfen başka bir klasor ismi giriniz.",HttpStatus.BAD_REQUEST),
    FOLDER_CANNOT_DELETE(203,"Bu kalasor silinemez!",HttpStatus.BAD_REQUEST);




    int code;
    String message;
    HttpStatus httpStatus;
}