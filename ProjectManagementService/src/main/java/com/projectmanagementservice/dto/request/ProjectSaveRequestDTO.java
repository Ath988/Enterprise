package com.projectmanagementservice.dto.request;

import java.time.LocalDate;


public record ProjectSaveRequestDTO(
        String token,
        String name,
        String description,
        LocalDate startDate,
        LocalDate endDate)
{
}
