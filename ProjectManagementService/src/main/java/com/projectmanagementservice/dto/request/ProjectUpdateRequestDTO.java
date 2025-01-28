package com.projectmanagementservice.dto.request;

import java.time.LocalDate;

public record ProjectUpdateRequestDTO(Long id,String name, String description, LocalDate startDate, LocalDate endDate)
{
}
