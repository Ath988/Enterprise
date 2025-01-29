package com.bilgeadam.dto.request;

public record AddNewDepartmentRequest(

        String departmentName,
        String description,
        Long parentDepartmentId


) {
}
