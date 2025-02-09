package com.bilgeadam.dto.request;

import com.bilgeadam.entity.enums.ETaxType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TaxRecordUpdateRequestDTO(
        Long id,
        Long accountId,
        ETaxType taxType,
        BigDecimal taxAmount,
        LocalDate taxPeriodStart,
        LocalDate taxPeriodEnd,
        LocalDate paymentDate,
        BigDecimal taxRate,
        String description
) {
}
