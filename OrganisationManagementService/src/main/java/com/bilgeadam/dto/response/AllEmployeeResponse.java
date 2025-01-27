package com.bilgeadam.dto.response;

import com.bilgeadam.entity.enums.EmployeeRole;

//Liste olarak dönen çalışanlara ait özet bilgiler, genişletilebilir
public record AllEmployeeResponse(

        Long employeeId,
        String firstName,
        String lastName,
        EmployeeRole employeeRole,
        String departmentName

) {



}
