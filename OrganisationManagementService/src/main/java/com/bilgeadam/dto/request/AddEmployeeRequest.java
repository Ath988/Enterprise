package com.bilgeadam.dto.request;

import com.bilgeadam.entity.enums.EmployeeRole;

public record AddEmployeeRequest(
        String token,
        String firstName,
        String lastName,
        String email,
        EmployeeRole role,
        Long departmentId
) {
}
