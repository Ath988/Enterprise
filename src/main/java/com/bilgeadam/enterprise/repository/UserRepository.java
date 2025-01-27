package com.bilgeadam.enterprise.repository;

import com.bilgeadam.enterprise.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, String> {
	
	Set<User> findUserByIdIn(Set<String> userIds);
	
	Optional<User> findUserById(String id);
	
	@Query("SELECT u.id FROM User AS u WHERE u.email=?1 AND u.password=?2")
	Optional<String> findIdByUsernameAndPassword(String mail, String password);
}