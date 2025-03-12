package com.inventoryservice.entities;


import com.inventoryservice.entities.enums.EStockMovementType;
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
@Table(name = "tblstockmovement")
public class StockMovement extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long authId;
    String description;
    @ManyToOne
    Product product;
    Integer quantity;
    BigDecimal total;
    @Enumerated(EnumType.STRING)
    EStockMovementType type;

    @PrePersist
    @PreUpdate
    public void calculateTotalPrice()
    {
        total = product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }


}
