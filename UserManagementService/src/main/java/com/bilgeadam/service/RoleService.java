package com.bilgeadam.service;

import com.bilgeadam.entity.Role;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.UserManagementException;
import com.bilgeadam.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role findByName(String roleName) {
        return roleRepository.findByName(roleName).orElseThrow(()->new UserManagementException(ErrorType.ROLE_NOT_FOUND));
    }

}
