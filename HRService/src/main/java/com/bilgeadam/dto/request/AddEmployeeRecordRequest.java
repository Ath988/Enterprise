package com.bilgeadam.dto.request;

import com.bilgeadam.dto.request.otherServices.AddEmployeeRequest;

import java.time.LocalDate;

public record AddEmployeeRecordRequest(

        AddEmployeeRequest addEmployeeRequest,
        LocalDate startDate,
        LocalDate endDate

) {
}
