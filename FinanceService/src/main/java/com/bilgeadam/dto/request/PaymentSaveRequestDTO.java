package com.bilgeadam.dto.request;

import com.bilgeadam.entity.enums.EPayment;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentSaveRequestDTO(
        Long accountId,
        Long invoiceId,
        EPayment paymentMethod,
        BigDecimal amount,
        LocalDate paymentDate,
        Boolean isPaid
) {
}
