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
	Long id;
	
	@Column(name = "performer_first_name")
	String firstName;
	
	@Column(name = "performer_last_name")
	String lastName;
	
	@Column(name = "performer_email")
	String email;
	
	@Column(name = "performer_phone_number")
	String phoneNumber;
}