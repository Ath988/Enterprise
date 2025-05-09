package com.bilgeadam.dto.response;

import com.bilgeadam.dto.response.otherServices.EmployeeDetailResponse;
import com.bilgeadam.entity.EmployeeRecord;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class EmployeeRecordResponse {

    public EmployeeRecordResponse(Long id,Long employeeId,LocalDate startDate, LocalDate endDate,String personelFileName,String personelFileUrl) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.employeeId = employeeId;
        this.personelFileName = personelFileName;
        this.personelFileUrl = personelFileUrl;
    }


    Long id;
    Long employeeId;
    LocalDate startDate;
    LocalDate endDate;
    String personelFileName;
    String personelFileUrl;

    EmployeeDetailResponse employeeDetailResponse;

}
