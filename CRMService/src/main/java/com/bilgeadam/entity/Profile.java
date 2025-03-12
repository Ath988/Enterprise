package com.bilgeadam.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Profile {
	String firstName;
	String lastName;
	@Column(unique = true, nullable = false)
	String email;
	@Column(unique = true, nullable = false)
	String phoneNumber;
	String address;
}