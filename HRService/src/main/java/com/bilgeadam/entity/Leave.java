package com.bilgeadam.entity;

import com.bilgeadam.entity.enums.ELeaveType;
import com.bilgeadam.entity.enums.EStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "tbl_leave")
public class Leave extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long employeeId;
    @Enumerated(EnumType.STRING)
    ELeaveType leaveType;
    String reason;
    LocalDate startDate;
    LocalDate endDate;
    int duration;
    @Enumerated(EnumType.STRING)
    EStatus status;
    Long approvedBy;
    LocalDate approvedAt;
    String response;
}
