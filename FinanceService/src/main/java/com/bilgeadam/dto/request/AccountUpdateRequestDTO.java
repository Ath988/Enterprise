package com.bilgeadam.dto.request;

import com.bilgeadam.entity.enums.ECurrency;

import java.math.BigDecimal;

public record AccountUpdateRequestDTO(
        Long id,
        String companyName,
        String bankName,
        String accountName,
        String accountNumber,
        ECurrency currency,
        BigDecimal balance) {
}
