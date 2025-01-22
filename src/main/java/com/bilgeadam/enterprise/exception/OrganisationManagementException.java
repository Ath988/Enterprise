package com.bilgeadam.enterprise.exception;

import lombok.Getter;

@Getter
public class OrganisationManagementException extends RuntimeException {
    private ErrorType errorType;
    public OrganisationManagementException(ErrorType errorType){
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}
