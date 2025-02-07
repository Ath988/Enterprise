package com.inventoryservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType
{
    VALIDATION_ERROR(400, "girilen parametreler hatalıdır. Lütfen kontrol ederek tekrar deneyin.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR(500, "Sunucuda beklenmeyen bir hata oldu. Lütfen tekrar deneyin", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_TOKEN(1007,"Girdiğiniz token hatalıdır. Lütfen değiştirerek tekrar deneyiniz.",HttpStatus.UNAUTHORIZED),

    WAREHOUSE_NOT_FOUND(9002,"Ware House Not Found",HttpStatus.BAD_REQUEST),

    PRODUCT_NOT_FOUND(9003,"Product Not Found",HttpStatus.BAD_REQUEST),

    INSUFFICIENT_STOCK(9004,"Insufficient Stock",HttpStatus.BAD_REQUEST),

    PRODUCT_NOT_ACTIVE(9005,"Product Not Active",HttpStatus.BAD_REQUEST),

    ORDER_NOT_FOUND(9006,"Order Not Found",HttpStatus.BAD_REQUEST),

    PRODUCT_CATEGORY_NOT_FOUND(9007,"Product Category Not Found",HttpStatus.BAD_REQUEST),

    STOCK_MOVEMENT_NOT_FOUND(9008,"Stock Movement Not Found",HttpStatus.BAD_REQUEST),

    SUPPLIER_NOT_FOUND(9009,"Supplier Not Found",HttpStatus.BAD_REQUEST),

    VALUE_CAN_NOT_BE_BELOW_ZERO( 9010,"Value can not be below zero",HttpStatus.BAD_REQUEST),

    SUPPLIER_EMAIL_ALREADY_EXISTS( 9011,"Supplier Email Already Exists",HttpStatus.BAD_REQUEST);

    int code;
    String message;
    HttpStatus httpStatus;
}
