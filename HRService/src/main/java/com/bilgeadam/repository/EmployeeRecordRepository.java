package com.bilgeadam.repository;

import com.bilgeadam.dto.response.AllEmployeeRecordResponse;
import com.bilgeadam.dto.response.EmployeeRecordResponse;
import com.bilgeadam.entity.EmployeeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmployeeRecordRepository extends JpaRepository<EmployeeRecord,Long> {

    @Query("SELECT new com.bilgeadam.dto.response.EmployeeRecordResponse(e.id,e.employeeId,e.startDate,e.endDate) FROM EmployeeRecord e WHERE e.id = ?1")
    Optional<EmployeeRecordResponse> findEmployeeRecordResponseById(Long id);

    @Query("SELECT NEW com.bilgeadam.dto.response.AllEmployeeRecordResponse(e.id,e.startDate,e.endDate) FROM EmployeeRecord e WHERE e.employeeId = ?1")
    Optional<AllEmployeeRecordResponse> findAllEmployeeRecordResponse(Long employeeId);


}
