package com.bilgeadam.dto.request;

import com.bilgeadam.entity.enums.ETaxType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TaxRecordSaveRequestDTO(
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
