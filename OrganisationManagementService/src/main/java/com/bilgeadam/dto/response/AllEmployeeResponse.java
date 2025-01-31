package com.bilgeadam.dto.response;

//Liste olarak dönen çalışanlara ait özet bilgiler, genişletilebilir
public record AllEmployeeResponse(

        Long employeeId,
        String firstName,
        String lastName,
        String positionName,
        String departmentName

) {



}
