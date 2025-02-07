package com.bilgeadam.dto.response;

import com.bilgeadam.entity.Account;
import com.bilgeadam.entity.enums.ECurrency;

import java.math.BigDecimal;

public record AccountResponseDTO(
        Long id,
        String companyName,
        String bankName,
        String accountNumber,
        ECurrency currency,
        BigDecimal balance
) {
    public AccountResponseDTO(Account account) {
        this(
                account.getId(),
                account.getCompanyName(),
                account.getBankName(),
                account.getAccountNumber(),
                account.getCurrency(),
                account.getBalance()
        );
    }
}
