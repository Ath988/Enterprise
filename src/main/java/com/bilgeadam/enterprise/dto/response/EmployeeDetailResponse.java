package com.bilgeadam.enterprise.dto.response;

import com.bilgeadam.enterprise.entity.enums.EmployeeRole;

import java.time.LocalDate;

public record EmployeeDetailResponse(

        String firstName,
        String lastName,
        String email,
        LocalDate hireDate,
        EmployeeRole employeeRole,
        String departmentName


) {

}
