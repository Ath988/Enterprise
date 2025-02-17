package com.bilgeadam.entity;

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
	String email;
	String phoneNumber;
	String address;
}