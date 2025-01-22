package com.bilgeadam.enterprise.dto.request;

import com.bilgeadam.enterprise.entity.enums.EmployeeRole;

import java.time.LocalDateTime;

public record AddEmployeeRequest(
        String token,
        String firstName,
        String lastName,
        String email,
        EmployeeRole role,
        Long departmentId
) {
}
