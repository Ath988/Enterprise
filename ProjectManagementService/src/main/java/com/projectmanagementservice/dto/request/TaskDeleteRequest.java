package com.projectmanagementservice.dto.request;

public record TaskDeleteRequest(
        String token,
        Long taskId
) {
}
