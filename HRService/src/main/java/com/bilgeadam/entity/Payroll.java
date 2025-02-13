package com.bilgeadam.entity;

import com.bilgeadam.entity.enums.ECurrency;
import com.bilgeadam.entity.enums.EStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "tbl_payroll")
public class Payroll extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long employeeId;
    LocalDate salaryDate;
    Double grossSalary;
    Double deductions;
    Double netSalary;
    @Enumerated(EnumType.STRING)
    ECurrency salaryCurrency;
    @Enumerated(EnumType.STRING)
    EStatus status;


}
