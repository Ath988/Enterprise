package com.bilgeadam.dto.response;

import com.bilgeadam.entity.enums.EAssetCategoryType;
import lombok.Builder;

@Builder
public record MaintenanceResponseDto(
        Long id,
        Long employeeId,
        String firstName,
        String lastName,
        String description,
        EAssetCategoryType type
) {
}
