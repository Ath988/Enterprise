package com.bilgeadam.repository;

import com.bilgeadam.dto.response.UserDetailsForChatResponse;
import com.bilgeadam.dto.response.UserProfileResponse;
import com.bilgeadam.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByAuthId(Long authId);

    @Query("SELECT u2.id FROM User u1 " +
            "JOIN User u2 ON u1.companyId = u2.companyId " +
            "JOIN UserRolePermission urp ON urp.user = u2 " +
            "WHERE u1.id = ?1 AND urp.role.name = ?2")
    Optional<Long> findMemberIdByUserId(Long userId, String roleName);

    @Query("SELECT NEW com.bilgeadam.dto.response.UserProfileResponse(u.id,u.authId,u.firstName,u.lastName,u.phoneNo,u.tcNo,u.email,u.birthDate,u.avatarUrl) FROM User u WHERe u.id = ?1")
    UserProfileResponse findUserProfileById(Long id);
    
    @Query("SELECT new com.bilgeadam.dto.response.UserDetailsForChatResponse(e.id, e.companyId, e.firstName, e" +
            ".lastName, e.avatarUrl,e.isOnline) " +
            "FROM User e " +
            "WHERE e.companyId = :companyId " +
            "AND e.id != :employeeId " +
            "AND e.state = com.bilgeadam.entity.enums.EState.ACTIVE")
    List<UserDetailsForChatResponse> findAllActiveUsersByCompanyId(@Param("companyId") Long companyId,
                                                                       @Param("employeeId") Long employeeId);
    
    @Query("SELECT new com.bilgeadam.dto.response.UserDetailsForChatResponse(e.id, e.companyId, e.firstName, e" +
            ".lastName, e.avatarUrl,e.isOnline) " +
            "FROM User e " +
            "WHERE e.id IN (:ids) AND e.state = com.bilgeadam.entity.enums.EState.ACTIVE")
    List<UserDetailsForChatResponse> findUsersByIds(@Param("ids") List<Long> ids);
}