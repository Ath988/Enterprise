package com.bilgeadam.dto.request;

import com.bilgeadam.entity.enums.ECurrency;

import java.time.LocalDate;

public record AddPayrollRequest(

        Long employeeId,
        LocalDate payDate,
        Double grossSalary,
        Double deductions,
        Double netSalary,
        ECurrency currency



) {
}
