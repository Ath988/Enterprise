package com.bilgeadam.dto.request;

import com.bilgeadam.entity.enums.ELeaveType;

import java.time.LocalDate;

public record AddLeaveRequest(
        ELeaveType leaveType,
        String reason,
        LocalDate startDate,
        LocalDate endDate
) {
}
