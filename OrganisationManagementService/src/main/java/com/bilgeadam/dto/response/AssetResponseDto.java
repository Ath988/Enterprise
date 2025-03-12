package com.bilgeadam.dto.response;

import com.bilgeadam.entity.enums.EAssetCategoryType;
import com.bilgeadam.entity.enums.EAssetStatus;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record AssetResponseDto(
        Long id,
        Long employeeId,
        String firstName,
        String lastName,
        String description,
        EAssetCategoryType type,
        EAssetStatus status,
        LocalDate givenDate
) {
}
