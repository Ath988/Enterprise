package com.bilgeadam.repository;

import com.bilgeadam.entity.Performer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerformerRepository extends JpaRepository<Performer, Long> {
	Optional<Performer> findByEmail(String email);
	Optional<Performer> findByPhoneNumber(String phoneNumber);
}