package com.bilgeadam.subscriptionservice.entity;

import com.bilgeadam.subscriptionservice.entity.enums.EntityStatus;
import jakarta.persistence.*;
import lombok.Data;

@MappedSuperclass
@Data
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    EntityStatus status;
    Long createdAt;
    Long updatedAt;
}
