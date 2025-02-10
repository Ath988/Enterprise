package com.bilgeadam.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FinancialReportSaveRequestDTO(
        Long id,
        Long accountId,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal totalIncome,
        BigDecimal totalExpense,
        BigDecimal netProfit,
        String message
) {
}
