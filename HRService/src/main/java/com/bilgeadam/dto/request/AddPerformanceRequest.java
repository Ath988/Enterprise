package com.bilgeadam.dto.request;

import com.bilgeadam.entity.enums.EPerformanceGrade;

public record AddPerformanceRequest(

        Long employeeRecordId,
        EPerformanceGrade grade,
        String feedback,
        Integer trainingHours,
        String trainingTopics

) {
}
