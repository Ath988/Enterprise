package com.bilgeadam.entity;

import com.bilgeadam.entity.enums.ECurrency;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
// OKK

@EqualsAndHashCode(callSuper=true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="tbl_accounts") // HESAP
public class Account extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String accountName;
    String accountNumber;
    ECurrency currency;

    @Builder.Default
    BigDecimal balance = BigDecimal.ZERO;






}
