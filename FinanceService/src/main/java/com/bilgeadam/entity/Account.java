package com.bilgeadam.entity;

import com.bilgeadam.entity.enums.ECurrency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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

        String companyName;
        String bankName;
        String accountNumber;
        @Enumerated(EnumType.STRING)
        ECurrency currency; //Para Birimi
        BigDecimal balance;
}
