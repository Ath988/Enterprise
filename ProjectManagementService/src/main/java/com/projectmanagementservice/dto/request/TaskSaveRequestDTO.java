package com.projectmanagementservice.dto.request;

import com.projectmanagementservice.utility.ETaskPriorityStatus;
import com.projectmanagementservice.utility.ETaskStatus;

public record TaskSaveRequestDTO(
		String token,
		Long projectId,
        String name,
        String description,
		ETaskStatus taskStatus,
		ETaskPriorityStatus taskPriorityStatus,
		Long authId
)
{
}