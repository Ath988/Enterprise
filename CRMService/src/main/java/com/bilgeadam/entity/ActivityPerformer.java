package com.bilgeadam.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityPerformer {
	@Column(name = "performer_id")
	private Long id;
	
	@Column(name = "performer_name")
	private String name;
	
	@Column(name = "is_staff")
	private Boolean staff;
}