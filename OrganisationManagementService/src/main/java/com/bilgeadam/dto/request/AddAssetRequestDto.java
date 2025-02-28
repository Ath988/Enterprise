package com.bilgeadam.dto.request;

import com.bilgeadam.entity.enums.EAssetCategoryType;

import java.time.LocalDate;

public record AddAssetRequestDto(
        Long employeeId,
        String description,
        EAssetCategoryType type,
        LocalDate givenDate
) {
}
