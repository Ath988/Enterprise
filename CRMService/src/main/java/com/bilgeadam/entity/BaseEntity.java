package com.bilgeadam.entity;

import com.bilgeadam.entity.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	Status status = Status.ACTIVE;
	
	@CreationTimestamp
	LocalDateTime createdAt;
	@UpdateTimestamp
	LocalDateTime updatedAt;
}