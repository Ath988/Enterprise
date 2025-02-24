package com.bilgeadam.repository;

import com.bilgeadam.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByAuthId(Long authId);

    @Query("SELECT u2.id FROM User u1 " +
            "JOIN User u2 ON u1.companyId = u2.companyId " +
            "JOIN UserRolePermission urp ON urp.user = u2 " +
            "WHERE u1.id = ?1 AND urp.role.name = ?2")
    Optional<Long> findMemberIdByUserId(Long userId, String roleName);

}
