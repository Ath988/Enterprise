package com.bilgeadam.repository;

import com.bilgeadam.entity.UserRolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRolePermissionRepository extends JpaRepository<UserRolePermission, Long> {
    List<UserRolePermission> findAllByUserId(Long userId);
}
