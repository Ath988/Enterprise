package com.bilgeadam.dto.request;

import com.bilgeadam.entity.enums.ECurrency;

import java.math.BigDecimal;

public record AccountUpdateRequestDTO(
        Long id,
        BigDecimal balance,
        String accountName,
        String accountNumber,
        ECurrency currency
        ) {


}
