package com.bilgeadam.dto.response;

import com.bilgeadam.entity.enums.ELeaveType;
import com.bilgeadam.entity.enums.EStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter

public class LeaveDetailsResponse {

    LeaveDetailsResponse(Long id, Long employeeId,LocalDateTime requestDate,ELeaveType leaveType,String leaveReason,LocalDate startDate,LocalDate endDate,
                         int duration,EStatus status){
        this.id = id;
        this.employeeId = employeeId;
        this.requestDate = requestDate;
        this.leaveType = leaveType;
        this.leaveReason = leaveReason;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.status = status;
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
    EStatus status;
    String approvedBy;
    LocalDateTime approvedAt;
    String response;

}
