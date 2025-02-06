package com.bilgeadam.dto.response;

import com.bilgeadam.entity.Payroll;
import com.bilgeadam.entity.enums.ECurrency;
import com.bilgeadam.entity.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PayrollResponse {
    public PayrollResponse(Long id,Long employeeId,LocalDate salaryDate, Double grossSalary, Double deductions,
                           Double netSalary,ECurrency currency,EStatus status){
        this.id = id;
        this.employeeId = employeeId;
        this.salaryDate = salaryDate;
        this.grossSalary = grossSalary;
        this.deductions = deductions;
        this.netSalary = netSalary;
        this.currency = currency;
        this.status = status;
    }

    Long id;
    Long employeeId;
    String employeeName;
    LocalDate salaryDate;
    Double grossSalary;
    Double deductions;
    Double netSalary;
    ECurrency currency;
    EStatus status;


}
