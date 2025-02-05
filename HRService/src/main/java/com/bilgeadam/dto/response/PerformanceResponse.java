package com.bilgeadam.dto.response;

import com.bilgeadam.entity.enums.EPerformanceGrade;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PerformanceResponse {

    public PerformanceResponse(Long id, EPerformanceGrade grade,String feedback, Integer trainingHours,
                               String trainingTopics,LocalDateTime evaluationDate){
        this.id = id;
        this.grade = grade;
        this.feedback = feedback;
        this.trainingHours = trainingHours;
        this.trainingTopics = trainingTopics;
        this.evaluationDate = evaluationDate;
    }

    Long id;
    String employeeName;
    String evaulatedBy;
    EPerformanceGrade grade;
    String feedback;
    Integer trainingHours;
    String trainingTopics;
    LocalDateTime evaluationDate;
}
