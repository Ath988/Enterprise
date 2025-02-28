package com.bilgeadam.dto.request;

import com.bilgeadam.entity.enums.EAssetCategoryType;

public record CreateMaintenanceRequestDto(
        Long employeeId,
        EAssetCategoryType type,
        String description
) {
}
