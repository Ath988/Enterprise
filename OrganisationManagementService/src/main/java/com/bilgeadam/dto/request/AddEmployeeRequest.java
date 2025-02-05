package com.bilgeadam.dto.request;

import com.bilgeadam.dto.request.otherServices.RegisterRequestDto;
import com.bilgeadam.entity.enums.EGender;
import com.bilgeadam.entity.enums.EmployeeRole;

public record AddEmployeeRequest(
        String firstName,
        String lastName,
        EmployeeRole role,
        Long positionId,
        EGender gender,
        RegisterRequestDto registerRequestDto
) {
}
