package com.bilgeadam.dto.request;

public record CreateShiftRequestDto(
        String shiftName,
        String startTime,
        String endTime,
        String location
) {
}