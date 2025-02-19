package com.bilgeadam.service;

import com.bilgeadam.entity.UserRolePermission;
import com.bilgeadam.entity.enums.Permission;
import com.bilgeadam.repository.UserRolePermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRolePermissionService {
    private final UserRolePermissionRepository userRolePermissionRepository;

    public void save(UserRolePermission userRolePermission) {
        userRolePermissionRepository.save(userRolePermission);
    }

    public Set<String> findAllPermissionsByUserId(Long userId){
       List<UserRolePermission> userRolePermissions = userRolePermissionRepository.findAllByUserId(userId);
        return userRolePermissions.stream()
                .map(UserRolePermission::getPermissions)
                .flatMap(Set::stream)
                .map(Enum::name)
                .collect(Collectors.toSet());
    }

}
