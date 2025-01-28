package com.bilgeadam.dto.request;

import com.bilgeadam.entity.enums.EmployeeRole;

import java.util.List;

public record AssignEmployeesToDepartmentRequest(

        String token,
        List<Long> employeeIds,
        Long departmenId
) {
}
