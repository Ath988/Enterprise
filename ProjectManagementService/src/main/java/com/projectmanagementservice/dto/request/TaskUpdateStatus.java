package com.projectmanagementservice.dto.request;

import com.projectmanagementservice.utility.ETaskStatus;

public record TaskUpdateStatus(
		Long taskId,
		ETaskStatus taskStatus
) {
}