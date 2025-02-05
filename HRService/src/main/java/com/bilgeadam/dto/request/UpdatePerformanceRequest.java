package com.bilgeadam.dto.request;

import com.bilgeadam.entity.enums.EPerformanceGrade;

public record UpdatePerformanceRequest(
        Long performanceId,
        EPerformanceGrade grade,
        String feedback,
        Integer trainingHours,
        String trainingTopics
) {
}
