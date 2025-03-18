package com.projectmanagementservice.dto.request;

import com.projectmanagementservice.utility.ETaskPriorityStatus;
import com.projectmanagementservice.utility.ETaskStatus;

public record TaskUpdateRequestDTO(
		Long id,
		String name,
		String description,
		ETaskStatus taskStatus,
		ETaskPriorityStatus taskPriorityStatus)
{
}