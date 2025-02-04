package com.bilgeadam.dto.response;

public record PositionDetailResponse(
        Long id,
        String departmentName,
        String title,
        String description
) {
}
