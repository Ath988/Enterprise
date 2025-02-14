package com.bilgeadam.enterprise.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "tbluser")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	@Column(unique = true, nullable = false)
	private String email;
	private String password;
	private String name;
	private String surname;
	@Builder.Default
	private Boolean isOnline = false;
	private String profilePicture;
}