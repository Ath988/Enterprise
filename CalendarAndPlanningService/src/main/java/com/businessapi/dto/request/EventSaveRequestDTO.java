package com.businessapi.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record EventSaveRequestDTO(
        String title,
        @NotNull LocalDateTime startTime,
        @NotNull LocalDateTime endTime,
        Boolean allDay,
        String token) {
}
