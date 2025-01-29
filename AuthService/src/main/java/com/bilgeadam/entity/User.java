package com.bilgeadam.entity;

import com.bilgeadam.util.enums.Permission;
import com.bilgeadam.util.enums.Role;

import java.util.Set;

public class User {
    private Long id;
    private String username;
    private Role role;
    private Set<Permission> permissions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;

}

}
