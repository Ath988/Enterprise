package com.bilgeadam.dto.response;

import com.bilgeadam.entity.enums.EGender;

//Liste olarak dönen çalışanlara ait özet bilgiler, genişletilebilir
public record AllEmployeeResponse(

        Long employeeId,
        String firstName,
        String lastName,
        String positionName,
        String departmentName,
        EGender gender

) {



}
