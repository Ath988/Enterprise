package com.bilgeadam.dto.request;

import java.util.List;

public record AssignEmployeesToDepartmentRequest(

        List<Long> employeeIds,
        Long departmenId
) {
}
