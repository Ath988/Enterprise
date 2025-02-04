package com.bilgeadam.enterprise.repository;

import com.bilgeadam.enterprise.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, String> {
	
	Set<User> findUserByIdIn(Set<String> userIds);
	
	Optional<User> findUserById(String id);
	
	@Query("SELECT u.id FROM User AS u WHERE u.email=?1 AND u.password=?2")
	Optional<String> findIdByUsernameAndPassword(String mail, String password);
	
	@Query("""
    SELECT COUNT(u) = :size FROM User u
    WHERE u.id IN :userIds
""")
	boolean existsAllUsers(@Param("userIds") Set<String> userIds, @Param("size") long size);
	
	@Query("SELECT u FROM User u WHERE u.id IN :userIds")
	List<User> findUsersByIds(@Param("userIds") List<String> userIds);
	
	@Query("""
    SELECT u.id, u.name, u.surname
    FROM User u
    WHERE u.id IN :userIds
""")
	List<Object[]> findUserNamesByIds(@Param("userIds") Set<String> userIds);
	
}