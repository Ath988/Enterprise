package com.bilgeadam.ticketservice.entity;

import com.bilgeadam.ticketservice.entity.enums.EntityStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@Data
@SuperBuilder
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    com.bilgeadam.ticketservice.entity.enums.EntityStatus entityStatus = EntityStatus.ACTIVE;
    @Builder.Default
    Long createdAt = System.currentTimeMillis();
    @Builder.Default
    Long updatedAt = System.currentTimeMillis();
}
