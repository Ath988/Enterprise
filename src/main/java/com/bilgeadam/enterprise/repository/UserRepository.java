package com.bilgeadam.enterprise.repository;

import com.bilgeadam.enterprise.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, String> {
	
	Set<User> findUserByIdIn(List<String> userIds);
	
	Optional<User> findUserById(String id);
}