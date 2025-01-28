package com.bilgeadam.dto.request;

import com.bilgeadam.entity.enums.EmployeeRole;

public record UpdateDepartmentRequest(

        String token,
        Long departmentId,
        String departmentName,
        String description,
        Long parentDepartmentId

) {
}
