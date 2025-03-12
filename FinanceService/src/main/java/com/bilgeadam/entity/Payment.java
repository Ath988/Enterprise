package com.bilgeadam.entity;

import com.bilgeadam.entity.enums.EPayment;
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
@Table(name = "tbl_payment") //ODEMELER
public class Payment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long accountId;
    Long invoiceId;

    @Enumerated(EnumType.STRING)
    EPayment paymentMethod;
    BigDecimal amount;
    LocalDate paymentDate;
    Boolean isPaid;
}
