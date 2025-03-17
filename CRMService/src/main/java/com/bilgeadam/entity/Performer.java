package com.bilgeadam.entity;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "performers")
public class Performer extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@Column(nullable = false)
	String firstName;
	
	@Column(nullable = false)
	String lastName;
	
	@Column(nullable = false, unique = true)
	String email;
	
	@Column(nullable = false, unique = true)
	String phoneNumber;
}