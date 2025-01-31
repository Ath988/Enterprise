package com.bilgeadam.dto.response;

import java.time.LocalDateTime;

public record EmployeeDetailResponse(

        Long employeeId,
        String firstName,
        String lastName,
        String email,
        LocalDateTime hireDate,
        String positionName,
        String departmentName


) {

}
