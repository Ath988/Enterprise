package com.bilgeadam.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

public record AddWorkingHoursRequest(

        Long employeeId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime

) {
}
