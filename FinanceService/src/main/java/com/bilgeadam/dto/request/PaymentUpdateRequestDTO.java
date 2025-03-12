package com.bilgeadam.dto.request;

import com.bilgeadam.entity.enums.EPayment;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentUpdateRequestDTO(
        Long id,
        Long accountId,
        Long invoiceId,
        EPayment paymentMethod,
        BigDecimal amount,
        LocalDate paymentDate,
        Boolean isPaid
) {
}
