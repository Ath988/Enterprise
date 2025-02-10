package com.bilgeadam.repository;

import com.bilgeadam.dto.response.LeaveDetailsResponse;
import com.bilgeadam.dto.response.LeaveResponse;
import com.bilgeadam.entity.Leave;
import com.bilgeadam.entity.enums.EState;
import com.bilgeadam.entity.enums.EStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LeaveRepository extends JpaRepository<Leave, Long> {

    @Query("SELECT NEW com.bilgeadam.dto.response.LeaveResponse(l.id,l.employeeId,l.createAt,l.leaveType,l.reason,l.startDate,l.endDate,l.duration) FROM Leave l WHERE " +
            "l.employeeId IN (SELECT e.employeeId FROM EmployeeRecord e WHERE e.companyId = ?1) " +
            "AND l.status = ?2 AND l.state = ?3 ORDER BY l.createAt DESC")
    List<LeaveResponse> findAllPendingLeavesByCompanyId(Long companyId, EStatus status, EState state);


    @Query("SELECT l FROM Leave l WHERE l.employeeId = ?1 AND l.state = ?2 ORDER BY l.createAt DESC")
    List<Leave> findAllByEmployeeId(Long employeeId,EState state);

    @Query("SELECT DISTINCT l.approvedBy FROM Leave l WHERE l.employeeId = ?1 AND l.state = ?2")
    List<Long> findAllApprovedIdByEmployeeId(Long employeeId,EState state);

    @Query("SELECT NEW com.bilgeadam.dto.response.LeaveDetailsResponse(l.id,l.employeeId,l.createAt,l.leaveType,l.reason,l.startDate,l.endDate,l.duration,l.status)" +
            "FROM Leave l WHERE l.id = ?1")
    Optional<LeaveDetailsResponse> findLeaveDetailsById(Long leaveId);
}
