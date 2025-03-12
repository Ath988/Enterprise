package com.bilgeadam.service;

import com.bilgeadam.entity.UserRolePermission;
import com.bilgeadam.entity.enums.Permission;
import com.bilgeadam.repository.UserRolePermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.EnumSet;
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

    /**
     * Çalışan kaydı eklendiğinde çalışana verilecek sayfa izinleri listesi. Daha sonra yönetici tarafından bu izinler
     * genişletilip daraltılabilecek.
     * @return
     */
    public Set<Permission> getPermissionsForEmployee() {
        return Set.of(
                Permission.ACCESS_PROFILE,
                Permission.ACCESS_CALENDAR_AND_CHAT
        );
    }


    public Set<String> findAllPermissionsByUserId(Long userId) {
        List<UserRolePermission> userRolePermissions = userRolePermissionRepository.findAllByUserId(userId);
        return userRolePermissions.stream()
                .map(UserRolePermission::getPermissions)
                .flatMap(Set::stream)
                .map(Enum::name)
                .collect(Collectors.toSet());
    }


    public List<UserRolePermission> findByAuthIdList(List<Long> authId) {
        return userRolePermissionRepository.findUserRolePermissionsByAuthIdList(authId);

    }

    public void saveAll(List<UserRolePermission> permissionsList) {
        userRolePermissionRepository.saveAll(permissionsList);
    }


}
