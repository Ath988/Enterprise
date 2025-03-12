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

    public AllEmployeeRecordResponse(Long employeeRecordId, LocalDate startDate, LocalDate endDate,String personelFileName,String personelFileUrl) {
        this.employeeRecordId = employeeRecordId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.personelFileName = personelFileName;
        this.personelFileUrl = personelFileUrl;
    }


    Long employeeRecordId;
    LocalDate startDate;
    LocalDate endDate;
    String personelFileName;
    String personelFileUrl;
    AllEmployeeResponse allEmployeeResponse;
}
