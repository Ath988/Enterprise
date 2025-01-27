package com.bilgeadam.dto.request;

import com.bilgeadam.entity.enums.EmployeeRole;

public record UpdateEmployeeRequest(
        String token,
        Long employeeId,
        String firstName,
        String lastName,
        String email,
        EmployeeRole role,
        Long departmentId

) {
}
