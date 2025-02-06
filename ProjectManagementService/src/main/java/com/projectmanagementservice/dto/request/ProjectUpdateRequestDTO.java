package com.projectmanagementservice.dto.request;

import java.time.LocalDate;

public record ProjectUpdateRequestDTO(
        String token,
        Long id,
        String name,
        String description,
        LocalDate startDate,
        LocalDate endDate)
{
}
