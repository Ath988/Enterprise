package com.bilgeadam.dto.request;

public record UpdateDepartmentManagerRequest(

        Long managerId,
        Long departmentId

) {
}
