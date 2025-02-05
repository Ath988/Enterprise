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
    @Column(nullable = false)
    Long employeeId;
    @Column(nullable = false)
    Long evaulatedBy; // değerlendirme yapanin employeeId
    @Enumerated(EnumType.STRING)
    EPerformanceGrade grade;
    String feedBack;
    Integer trainingHours;
    String trainingTopics;


}
