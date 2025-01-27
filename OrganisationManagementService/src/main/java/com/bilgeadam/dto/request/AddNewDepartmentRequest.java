package com.bilgeadam.dto.request;

public record AddNewDepartmentRequest(

        String token,
        String departmentName,
        String description,
        Long parentDepartmentId


) {
}
