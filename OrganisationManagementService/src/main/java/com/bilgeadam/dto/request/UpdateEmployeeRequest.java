package com.bilgeadam.dto.request;

import com.bilgeadam.entity.enums.EGender;
import com.bilgeadam.entity.enums.EmployeeRole;

public record UpdateEmployeeRequest(
        Long employeeId,
        String firstName,
        String lastName,
        String email,
        EmployeeRole role,
        EGender gender


) {
}
