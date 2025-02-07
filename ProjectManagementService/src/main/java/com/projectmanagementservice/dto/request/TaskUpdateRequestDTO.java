package com.projectmanagementservice.dto.request;

import java.time.LocalDate;

public record TaskUpdateRequestDTO(
        String token,
        Long id,
        Long authId,
        String name,
        String description)
{
}
