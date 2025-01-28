package com.bilgeadam.dto.request;

public record UpdateDepartmentRequest(

        Long departmentId,
        String departmentName,
        String description,
        Long parentDepartmentId

) {
}
