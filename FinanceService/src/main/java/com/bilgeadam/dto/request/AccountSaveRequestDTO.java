package com.bilgeadam.dto.request;

import com.bilgeadam.entity.enums.ECurrency;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.math.BigDecimal;

public record AccountSaveRequestDTO (
        String companyName,
        String accountName,
        String bankName,
        String accountNumber,
        ECurrency currency,
        BigDecimal balance
){
}
