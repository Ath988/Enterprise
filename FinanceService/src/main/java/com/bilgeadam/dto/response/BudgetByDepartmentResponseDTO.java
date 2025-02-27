package com.bilgeadam.dto.response;

import com.bilgeadam.entity.enums.EBudgetCategory;

import java.math.BigDecimal;

public record BudgetByDepartmentResponseDTO(Long id,
                                            EBudgetCategory budgetCategory,
                                            BigDecimal getSpentAmount,
                                            String description) {
}
