package com.projectmanagementservice.exceptions;

import lombok.Getter;

@Getter
public class ProjectManagementException extends RuntimeException{
    private ErrorType errorType;

    public ProjectManagementException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}
