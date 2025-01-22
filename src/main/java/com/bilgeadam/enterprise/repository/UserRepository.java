package com.bilgeadam.enterprise.repository;

import com.bilgeadam.enterprise.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findOptionalByEmail(String email);
}
