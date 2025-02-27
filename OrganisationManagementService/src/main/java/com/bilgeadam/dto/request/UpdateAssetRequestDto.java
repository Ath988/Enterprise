package com.bilgeadam.dto.request;

import com.bilgeadam.entity.enums.EAssetCategoryType;

import java.time.LocalDate;

public record UpdateAssetRequestDto(
        Long assetId,
        Long employeeId,
        String description,
        LocalDate givenDate,
        EAssetCategoryType type
) {
}
