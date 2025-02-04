package com.bilgeadam.dto.request;

public record UpdatePositionRequest(
        Long positionId,
        Long departmentId,
        String title,
        String description
) {
}
