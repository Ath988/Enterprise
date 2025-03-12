package com.bilgeadam.repository;

import com.bilgeadam.entity.UserRolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRolePermissionRepository extends JpaRepository<UserRolePermission, Long> {
    List<UserRolePermission> findAllByUserId(Long userId);

    @Query("SELECT urp FROM UserRolePermission urp WHERE urp.user.authId IN ?1")
    List<UserRolePermission> findUserRolePermissionsByAuthIdList(List<Long> authIdList);
}
