package com.bilgeadam.view;

import com.bilgeadam.entity.enums.EGender;
import com.bilgeadam.entity.enums.EmployeeRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record VwEmployee(

        Long employeeId,
        String employeeName,
        String avatarUrl,
        String email,
        EmployeeRole role,
		EGender gender
       


) {
}