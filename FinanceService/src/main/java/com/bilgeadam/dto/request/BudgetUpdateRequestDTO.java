package com.bilgeadam.dto.request;

import com.bilgeadam.entity.Transaction;
import com.bilgeadam.entity.enums.EBudgetCategory;

import java.math.BigDecimal;
import java.util.List;

public record BudgetUpdateRequestDTO(Long id,
                                     Integer year,
                                     Integer month,

                                     Long departmentId,
                                     EBudgetCategory budgetCategory,
                                     String description,
                                     BigDecimal allocatedAmount,
                                     BigDecimal spentAmount) {
}
