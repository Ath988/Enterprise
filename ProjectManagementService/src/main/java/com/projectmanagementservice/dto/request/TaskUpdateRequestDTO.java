package com.projectmanagementservice.dto.request;

import java.time.LocalDate;

public record TaskUpdateRequestDTO(Long id, String name, String description)
{
}
