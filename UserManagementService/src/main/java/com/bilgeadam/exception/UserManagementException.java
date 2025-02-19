package com.bilgeadam.exception;

import lombok.Getter;

@Getter
public class UserManagementException extends RuntimeException {
    private ErrorType errorType;
    public UserManagementException(ErrorType errorType) {
      super(errorType.getMessage());
      this.errorType = errorType;

    }
}
