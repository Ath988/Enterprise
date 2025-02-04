package com.bilgeadam.entity;

import com.bilgeadam.entity.enums.EPerformanceGrade;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "tbl_performance")
public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false, unique = true)
    Long employeeId;

    EPerformanceGrade grade;
    String feedBack;
    Integer trainingHours;
    String trainingTopics;


}
