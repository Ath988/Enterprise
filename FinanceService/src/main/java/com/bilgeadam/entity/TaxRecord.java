package com.bilgeadam.entity;

import com.bilgeadam.entity.enums.ETaxType;
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
@Table(name = "tbl_taxrecords") //VERGİ KAYITLARI
public class TaxRecord extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long accountId;
    @Enumerated(EnumType.STRING)
    ETaxType taxType;
    BigDecimal taxAmount;
    LocalDate taxPeriodStart;
    LocalDate taxPeriodEnd;
    LocalDate paymentDate;
    BigDecimal taxRate;
    String description;
}
