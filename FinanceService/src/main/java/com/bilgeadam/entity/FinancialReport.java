package com.bilgeadam.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
//OKK
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tbl_financialreport") // FİNANSAL RAPOR
public class FinancialReport extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;
     Long accountId;
     LocalDate startDate;
     LocalDate endDate;

     BigDecimal totalIncome;
     BigDecimal totalExpense;
     BigDecimal netProfit;  // Gelir - Gider = Net Kar
     String message;
}
