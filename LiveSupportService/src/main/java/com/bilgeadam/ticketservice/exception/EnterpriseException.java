package com.bilgeadam.ticketservice.exception;

import lombok.Getter;

@Getter
public class EnterpriseException extends RuntimeException{
    private ErrorType errorType;

    public EnterpriseException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
    public EnterpriseException(ErrorType errorType, String message){
        super(message);
        this.errorType = errorType;
    }
}
