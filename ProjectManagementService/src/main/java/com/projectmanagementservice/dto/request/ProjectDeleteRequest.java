package com.projectmanagementservice.dto.request;

public record ProjectDeleteRequest(
        String token,
        Long projectId
) {
}
