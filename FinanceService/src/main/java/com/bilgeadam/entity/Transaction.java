package com.bilgeadam.entity;

import com.bilgeadam.entity.enums.ETransactionType;
import jakarta.persistence.*;
import lombok.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    BigDecimal amount;
    LocalDate date;
    String description;
    Long accountId;
    Long budgetId;
    @Enumerated(EnumType.STRING)
    ETransactionType transactionType; // INCOME or EXPENSE




}
