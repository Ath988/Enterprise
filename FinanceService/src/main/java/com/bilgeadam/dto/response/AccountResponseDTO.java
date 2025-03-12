package com.bilgeadam.dto.response;

import com.bilgeadam.entity.enums.ECurrency;

import java.math.BigDecimal;

public record AccountResponseDTO(
        Long id,
        String accountName,
        String accountNumber,
        ECurrency currency,
        BigDecimal balance
) {
}
