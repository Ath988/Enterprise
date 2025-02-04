package com.bilgeadam.repository;

import com.bilgeadam.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    Optional<Auth> findByEmail(String email);

    Optional<Auth> findOptionalByEmail(String email);
    Optional<Auth> findOptionalById(Long authId);
}
