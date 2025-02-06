package com.bilgeadam.dto.request;

import com.bilgeadam.entity.enums.ECurrency;
import com.bilgeadam.entity.enums.EStatus;

import java.time.LocalDate;

public record UpdatePayrollRequest(

        Long payrollId,
        LocalDate payDate,
        Double grossSalary,
        Double deductions,
        Double netSalary,
        ECurrency currency,
        EStatus status

) {
}
