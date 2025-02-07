package com.bilgeadam.entity;

import com.bilgeadam.entity.enums.EntityStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Data
@SuperBuilder
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    EntityStatus status;
    Long createdAt;
    Long updatedAt;
}
