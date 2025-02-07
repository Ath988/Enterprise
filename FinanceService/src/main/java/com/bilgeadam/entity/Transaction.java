package com.bilgeadam.entity;

import com.bilgeadam.entity.enums.EExpenseCategory;
import com.bilgeadam.entity.enums.EIncomeCategory;
import com.bilgeadam.entity.enums.ETransactionType;
import jakarta.annotation.Nullable;
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
@Table(name = "tbl_transaction") //Gelir-Gider İşlemleri
public class Transaction extends BaseEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    Long id;

    Long accountId;
    Long invoiceId;
    @Enumerated(EnumType.STRING)
    ETransactionType transactionType; //Gelir ya da Gider

    @Enumerated(EnumType.STRING)
    @Nullable
    EExpenseCategory expenseCategory; // Harcama Kategorisi

    @Enumerated(EnumType.STRING)
    @Nullable
    EIncomeCategory incomeCategory; // Gelir Kategorisi

    BigDecimal amount;
    String description;
    LocalDate transactionDate;
}
