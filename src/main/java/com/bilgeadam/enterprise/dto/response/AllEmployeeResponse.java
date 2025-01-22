package com.bilgeadam.enterprise.dto.response;

import com.bilgeadam.enterprise.entity.enums.EmployeeRole;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

//Liste olarak dönen çalışanlara ait özet bilgiler, genişletilebilir
public record AllEmployeeResponse(

        String firstName,
        String lastName,
        EmployeeRole employeeRole,
        String departmentName

) {



}
