package com.bilgeadam.exception;

import lombok.Getter;

@Getter
public class HRException extends RuntimeException {
    private ErrorType errorType;
    public HRException(ErrorType errorType){
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}
