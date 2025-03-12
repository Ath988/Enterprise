package com.bilgeadam.dto.request;

import java.util.List;

public record AssignPositionToEmployeeListRequest(
        List<Long> employeeIdList,
        Long positionId
) {
}
