package com.bilgeadam.entity;

import com.bilgeadam.entity.enums.EPerformanceGrade;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "tbl_performance")
public class Performance extends BaseEntity {
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
