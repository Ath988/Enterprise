package com.bilgeadam.service;

import com.bilgeadam.entity.User;
import com.bilgeadam.util.enums.Permission;
import com.bilgeadam.util.enums.ERole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    public Set<Permission> getPermissionsForRole(ERole role) {
        switch (role) {
            case SYSTEM_ADMIN:
                return EnumSet.allOf(Permission.class);
            case MEMBER:
                return EnumSet.of(
                        Permission.ACCESS_CRM,
                        Permission.ACCESS_INVENTORY_MANAGEMENT,
                        Permission.ACCESS_FINANCE_AND_ACCOUNTING,
                        Permission.ACCESS_ORGANIZATION_MANAGEMENT,
                        Permission.ACCESS_CALENDAR_AND_CHAT,
                        Permission.ACCESS_PROJECT_MANAGEMENT,
                        Permission.ACCESS_HUMAN_RESOURCES
                );
            case STAFF:
                return EnumSet.of(
                        Permission.TRACK_TASKS_AND_PROJECTS,
                        Permission.MANAGE_PROFILE_AND_WORK_HOURS,
                        Permission.ACCESS_CALENDAR_AND_CHAT
                );
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }

    public void assignRoleToUser(User user, ERole role) {
        user.setRole(role);
        user.setPermissions(getPermissionsForRole(role));
    }

}
