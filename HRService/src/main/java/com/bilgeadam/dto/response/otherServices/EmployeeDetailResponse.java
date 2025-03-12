package com.bilgeadam.dto.response.otherServices;

import com.bilgeadam.entity.enums.EGender;

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
