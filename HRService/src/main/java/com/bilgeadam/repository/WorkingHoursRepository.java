package com.bilgeadam.repository;

import com.bilgeadam.dto.response.WorkingHoursResponse;
import com.bilgeadam.entity.WorkingHours;
import com.bilgeadam.entity.enums.EState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WorkingHoursRepository extends JpaRepository<WorkingHours,Long> {

    @Query("SELECT w FROM WorkingHours w WHERE w.employeeId IN " +
            "(SELECT e.employeeId FROM EmployeeRecord e WHERE e.companyId = ?1) AND w.state = ?2")
    List<WorkingHours> findAllByCompanyId(Long companyId, EState state);

    @Query("SELECT NEW com.bilgeadam.dto.response.WorkingHoursResponse(w.id,w.date,w.startTime,w.endTime,w.status) FROM WorkingHours w WHERE " +
            "w.employeeId = ?1 AND w.state = ?2")
    List<WorkingHoursResponse> findAllByEmployeeId(Long employeeId, EState state);


    Optional<WorkingHours> findByEmployeeId(Long employeeId);

}
