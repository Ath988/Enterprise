package com.bilgeadam.exception;

import lombok.Getter;

@Getter
public class DashboardException extends RuntimeException {
    private ErrorType errorType;
    public DashboardException(ErrorType errorType){
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}
