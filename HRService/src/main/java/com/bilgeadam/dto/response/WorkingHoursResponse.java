package com.bilgeadam.dto.response;

import com.bilgeadam.entity.enums.EStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public record WorkingHoursResponse(
        Long id,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        EStatus status

) {


}
