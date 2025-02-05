package com.bilgeadam.dto.response;

import com.bilgeadam.dto.response.otherServices.AllEmployeeResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AllEmployeeRecordResponse {

    public AllEmployeeRecordResponse(Long employeeRecordId, LocalDate startDate, LocalDate endDate) {
        this.employeeRecordId = employeeRecordId;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    Long employeeRecordId;
    LocalDate startDate;
    LocalDate endDate;
    AllEmployeeResponse allEmployeeResponse;
}
