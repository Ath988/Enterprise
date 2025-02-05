package com.bilgeadam.dto.request;

import com.bilgeadam.dto.request.otherServices.UpdateEmployeeRequest;

import java.time.LocalDate;

public record UpdateEmployeeRecordRequest(

        Long employeeRecordId,
        LocalDate startDate,
        LocalDate endDate,
        UpdateEmployeeRequest updateEmployeeRequest


) {
}
