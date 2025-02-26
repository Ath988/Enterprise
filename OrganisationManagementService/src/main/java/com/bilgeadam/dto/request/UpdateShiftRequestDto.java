package com.bilgeadam.dto.request;

public record UpdateShiftRequestDto(
        Long shiftId,
        Long employeeId,
        String shiftType,
        String startTime,
        String endTime,
        String location
) {
}
