package com.bilgeadam.dto.response;

import com.bilgeadam.entity.enums.ELeaveType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class LeaveResponse{

    public LeaveResponse(Long id, Long employeeId, LocalDateTime requestDate, ELeaveType leaveType, String leaveReason, LocalDate startDate,
                         LocalDate endDate, int duration){
        this.id = id;
        this.employeeId = employeeId;
        this.requestDate = requestDate;
        this.leaveType = leaveType;
        this.leaveReason = leaveReason;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
    }

    Long id;
    Long employeeId;
    LocalDateTime requestDate;
    String employeeFullName;
    ELeaveType leaveType;
    String leaveReason;
    LocalDate startDate;
    LocalDate endDate;
    int duration;




}
