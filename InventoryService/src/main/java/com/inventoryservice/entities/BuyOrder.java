package com.inventoryservice.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tblorder")
public class BuyOrder extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long authId;
    @ManyToOne
    Supplier supplier;
    @ManyToOne
    Product product;
    Integer quantity;
    BigDecimal unitPrice;
    BigDecimal total;

    @PrePersist
    @PreUpdate
    public void calculateTotalPrice() {
        // Calculation of Total Price
        if (unitPrice != null && quantity != null) {
            total = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }




}
