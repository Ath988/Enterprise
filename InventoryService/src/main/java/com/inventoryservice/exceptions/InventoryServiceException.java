package com.inventoryservice.exceptions;

import lombok.Getter;

@Getter
public class InventoryServiceException extends RuntimeException{
    private ErrorType errorType;

    public InventoryServiceException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}
