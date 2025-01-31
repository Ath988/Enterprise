package com.bilgeadam.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@MappedSuperclass
public abstract class BaseEntity
{
	
	@CreationTimestamp
	LocalDateTime createdAt;
	@UpdateTimestamp
	LocalDateTime updatedAt;
}