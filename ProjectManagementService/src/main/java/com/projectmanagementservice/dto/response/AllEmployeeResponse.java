package com.projectmanagementservice.dto.response;


import com.projectmanagementservice.entities.enums.EGender;
import com.projectmanagementservice.entities.enums.EState;

//Liste olarak dönen çalışanlara ait özet bilgiler, genişletilebilir
public record AllEmployeeResponse(

        Long employeeId,
        String firstName,
        String lastName,
        String positionName,
        String departmentName,
        EGender gender,
		EState state

) {



}