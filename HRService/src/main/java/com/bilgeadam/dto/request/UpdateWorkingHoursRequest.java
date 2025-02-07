package com.bilgeadam.dto.request;

import com.bilgeadam.entity.enums.EStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public record UpdateWorkingHoursRequest(
        Long id,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        EStatus status
) {
}
