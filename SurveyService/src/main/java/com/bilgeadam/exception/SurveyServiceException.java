package com.bilgeadam.exception;

import lombok.Getter;

@Getter
public class SurveyServiceException extends RuntimeException{
    private ErrorType errorType;
    public SurveyServiceException(ErrorType errorType){
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}