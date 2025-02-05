package com.bilgeadam.dto.response;

import com.bilgeadam.entity.enums.EGender;

import java.time.LocalDateTime;

public record EmployeeDetailResponse(

        Long employeeId,
        String firstName,
        String lastName,
        String email,
        String positionName,
        String departmentName,
        EGender gender


) {

}
