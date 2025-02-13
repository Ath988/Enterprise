package com.bilgeadam.dto.request;


import java.math.BigDecimal;

public record CalculateTaxRequestDTO(
        Long id,
        BigDecimal amount
) {
}