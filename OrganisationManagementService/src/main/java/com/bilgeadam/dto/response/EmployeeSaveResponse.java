package com.bilgeadam.dto.response;

/*
    Diğer servislerden employee kaydetmek için bu verileri dönüyoruz.
 */
public record EmployeeSaveResponse(

        Long employeeId,
        Long companyId

) {
}
