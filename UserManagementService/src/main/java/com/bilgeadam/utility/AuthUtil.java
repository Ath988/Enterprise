package com.bilgeadam.utility;

import com.bilgeadam.entity.Role;
import com.bilgeadam.entity.enums.Permission;
import com.bilgeadam.entity.enums.SubscriptionPlan;
import com.bilgeadam.repository.RoleRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.PostConstruct;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Getter
public class AuthUtil {

    @JsonIgnore
    private final RoleRepository roleRepository;

    Map<String, String> permissionAuth = new HashMap<>();
    Map<String, Set<String>> roleAuth = new HashMap<>();
    Map<String, Set<String>> subAuth = new HashMap<>();

    @PostConstruct
    private void init() {
        permissionAuth = getAllPermissions();
        roleAuth = getAllRoles();
        subAuth = getAllSubPlans();
    }


    private Map<String, Set<String>> getAllSubPlans() {
        List<String> subscriptionPlanList = Arrays.stream(SubscriptionPlan.values()).map(SubscriptionPlan::name).toList();
        Map<String, Set<String>> subAuth = new HashMap<>();
        subscriptionPlanList.forEach(sub -> {
            switch (sub) {
                case "BASIC" -> subAuth.put("BASIC", Set.of("BASIC", "PRO", "ENTERPRISE"));
                case "PRO" -> subAuth.put("PRO", Set.of("PRO", "ENTERPRISE"));
                case "ENTERPRISE" -> subAuth.put("ENTERPRISE", Set.of("ENTERPRISE"));
                default -> subAuth.put(sub, Set.of(sub));
            }
        });
        return subAuth;
    }


    private Map<String, Set<String>> getAllRoles() {
        List<Role> roleList = roleRepository.findAll();

        Map<String, Set<String>> roleAuth = new HashMap<>();
        for (Role role : roleList) {
            switch (role.getName()) {
                case "ADMIN" -> roleAuth.put("ADMIN", Set.of("SYSTEM_ADMIN"));
                case "MEMBER" -> roleAuth.put("MEMBER", Set.of("SYSTEM_ADMIN", "MEMBER"));
                case "STAFF" -> roleAuth.put("STAFF", Set.of("SYSTEM_ADMIN", "MEMBER", "STAFF"));
                default -> roleAuth.put(role.getName(), Set.of(role.getName()));
            }
        }

        return roleAuth;
    }


    private Map<String, String> getAllPermissions() {
        return Arrays.stream(Permission.values())
                .collect(Collectors.toMap(Permission::getPermissionTitle, Enum::name));
    }


}
