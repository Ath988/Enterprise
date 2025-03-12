package com.bilgeadam.repository;

import com.bilgeadam.dto.response.PerformanceResponse;
import com.bilgeadam.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    @Query("SELECT NEW com.bilgeadam.dto.response.PerformanceResponse(p.id,p.employeeId,p.grade,p.feedBack,p.trainingHours,p.trainingTopics,p.createAt) FROM Performance p WHERE p.id = ?1")
    Optional<PerformanceResponse> findPerformanceResponseByPerformanceId(Long performanceId);

    @Query("SELECT p.employeeId FROM Performance p WHERE p.id = ?1")
    Optional<Long> findEmployeeIdByPerformanceId(Long performanceId);

    @Query("SELECT p FROM Performance p WHERE p.employeeId IN (SELECT e.employeeId FROM EmployeeRecord e WHERE e.companyId = ?1) AND p.state = com.bilgeadam.entity.enums.EState.ACTIVE")
    List<Performance> findAllPerformancesByCompanyId(Long companyId);
}
