package com.bilgeadam.dto.request;

import com.bilgeadam.entity.enums.ECurrency;

import java.math.BigDecimal;

public record AccountSaveRequestDTO (

        String accountName,
        String accountNumber,
        ECurrency currency,
        BigDecimal balance
){
}
