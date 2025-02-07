package com.bilgeadam.dto.request;

import com.bilgeadam.entity.enums.EExpenseCategory;
import com.bilgeadam.entity.enums.EIncomeCategory;
import com.bilgeadam.entity.enums.ETransactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionSaveRequestDTO(
        Long accountId,
        Long invoiceId,
        ETransactionType transactionType,
        EExpenseCategory expenseCategory,
        EIncomeCategory incomeCategory,
        BigDecimal amount,
        String description,
        LocalDate transactionDate) {
}
