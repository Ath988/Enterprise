package com.bilgeadam.dto.request;

import com.bilgeadam.entity.enums.EAssetCategoryType;
import lombok.Builder;

@Builder
public record UpdateMaintenanceRequestDto(
        Long id,
        Long employeeId,
        String description,
        EAssetCategoryType type
) {
}
