package com.bilgeadam.dto.request;

import com.bilgeadam.entity.enums.EBudgetCategory;
import java.math.BigDecimal;


public record BudgetSaveRequestDTO(
        Long id,
        Integer year,
        Integer month,

        Long departmentId,
        EBudgetCategory budgetCategory,
        String description,
        BigDecimal allocatedAmount,
        BigDecimal spentAmount) {
}



