package com.projectmanagementservice.dto.request;

import java.time.LocalDate;

public record TaskSaveRequestDTO(
        String token,
        Long projectId,
        String name,
        String description)
{
}
