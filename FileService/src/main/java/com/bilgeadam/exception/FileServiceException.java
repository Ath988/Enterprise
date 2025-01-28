package com.bilgeadam.exception;

import lombok.Getter;

@Getter
public class FileServiceException extends RuntimeException{
    private ErrorType errorType;
    public FileServiceException(ErrorType errorType){
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}