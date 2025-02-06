package com.inventoryservice.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tblproduct")
public class Product extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long authId;
    @ManyToOne
    Supplier supplier;
    @ManyToOne
    WareHouse wareHouse;
    String name;
    String description;
    BigDecimal price;
    Integer stockCount;
    Integer minimumStockLevel;
}
