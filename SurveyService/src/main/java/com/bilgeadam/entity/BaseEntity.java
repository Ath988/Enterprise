package com.bilgeadam.entity;

import com.bilgeadam.entity.enums.EState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {
	@Id
	@Column(length = 36, unique = true, nullable = false)
	String id;
	
	Long createAt;
	
	Long updateAt;
	
	@Enumerated(EnumType.STRING)
	@Builder.Default
	EState state = EState.ACTIVE;
	
	@PrePersist
	protected void onCreate() {
		long now = System.currentTimeMillis();
		this.createAt = now;
		this.updateAt = now;
		if (id==null){
			id = UUID.randomUUID().toString();
		}
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.updateAt = System.currentTimeMillis();
	}
	
	
}