package com.bilgeadam.service;

import com.bilgeadam.dto.request.AddLeaveRequest;
import com.bilgeadam.dto.request.UpdateLeaveRequest;
import com.bilgeadam.dto.response.LeaveDetailsResponse;
import com.bilgeadam.dto.response.LeaveResponse;
import com.bilgeadam.entity.Leave;
import com.bilgeadam.entity.enums.EState;
import com.bilgeadam.entity.enums.EStatus;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.HRException;
import com.bilgeadam.manager.OrganisationManagementManager;
import com.bilgeadam.repository.LeaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.bilgeadam.dto.response.BaseResponse.*;

@Service
@RequiredArgsConstructor
public class LeaveService {
    private final LeaveRepository leaveRepository;
    private final EmployeeRecordService employeeRecordService;
    private final OrganisationManagementManager orm;

    public Boolean createLeaveRequest(String token, AddLeaveRequest dto) {
        checkDate(dto.startDate(), dto.endDate());

        Long employeeId = getDataFromResponse(orm.getEmployeeId(token));
        leaveRepository.save(Leave.builder()
                .employeeId(employeeId)
                .leaveType(dto.leaveType())
                .reason(dto.reason())
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .duration((int) ChronoUnit.DAYS.between(dto.startDate(), dto.endDate()))
                .status(EStatus.PENDING)
                .build());
        return true;
    }

    public List<LeaveResponse> findAllPendingLeaves(String token) {
        Long employeeId = getDataFromResponse(orm.getEmployeeId(token));

        Long companyId = employeeRecordService.findCompanyIdByEmployeeId(employeeId);

        List<LeaveResponse> leaveResponseList = leaveRepository.findAllPendingLeavesByCompanyId(companyId, EStatus.PENDING, EState.ACTIVE);
        Set<Long> employeeIdList = leaveResponseList.stream().map(LeaveResponse::getEmployeeId).collect(Collectors.toSet());

        Map<Long, String> map = getDataFromResponse(orm.getAllEmployeeNames(new ArrayList<>(employeeIdList)));
        for (LeaveResponse leaveResponse : leaveResponseList) {
            leaveResponse.setEmployeeFullName(map.get(leaveResponse.getEmployeeId()));
        }

        return leaveResponseList;
    }

    public List<LeaveDetailsResponse> findAllLeavesByEmployeeId(String token) {
        String employeeName = getDataFromResponse(orm.getEmployeeNameByToken(token, Optional.empty()));
        Long employeeId = getDataFromResponse(orm.getEmployeeId(token));
        List<Leave> leaveList = leaveRepository.findAllByEmployeeId(employeeId, EState.ACTIVE);

        List<Long> approvedByIdList = leaveRepository.findAllApprovedIdByEmployeeId(employeeId, EState.ACTIVE);
        Map<Long, String> approvedByNameMap = getDataFromResponse(orm.getAllEmployeeNames(approvedByIdList));

        return leaveList.stream().map(l -> {
            boolean isPending = l.getStatus() == EStatus.PENDING;
            return LeaveDetailsResponse.builder()
                    .leaveReason(l.getReason())
                    .leaveType(l.getLeaveType())
                    .requestDate(l.getCreateAt())
                    .startDate(l.getStartDate())
                    .endDate(l.getEndDate())
                    .duration(l.getDuration())
                    .status(l.getStatus())
                    .employeeId(l.getEmployeeId())
                    .employeeFullName(employeeName)
                    .approvedBy(isPending ? "-" : approvedByNameMap.get(l.getEmployeeId()))
                    .approvedAt(isPending ? null : l.getUpdateAt())
                    .response(isPending ? "-" : approvedByNameMap.get(l.getEmployeeId()))
                    .build();
        }).toList();

    }

    public LeaveDetailsResponse findLeaveDetails(String token, Long leaveId) {
        Leave leave = findById(leaveId);

        LeaveDetailsResponse response = leaveRepository.findLeaveDetailsById(leaveId).get();

        Long employeeId = response.getEmployeeId();
        checkCompany(token, employeeId);
        List<Long> employeeIdList = new ArrayList<>(List.of(leave.getEmployeeId()));
        if (leave.getApprovedBy() != null) {
            employeeIdList.add(leave.getApprovedBy());
        }
        Map<Long, String> fullNameMap = getDataFromResponse(orm.getAllEmployeeNames(employeeIdList));

        response.setEmployeeFullName(fullNameMap.get(employeeId));
        boolean isPending = leave.getStatus() == EStatus.PENDING;
        response.setApprovedBy(isPending ? "-" : fullNameMap.get(leave.getApprovedBy()));
        response.setApprovedAt(isPending ? null : leave.getUpdateAt());
        response.setResponse(isPending ? "-" : leave.getResponse());
        return response;
    }

    public Boolean updateLeaveRequest(String token, UpdateLeaveRequest dto) {
        Leave leave = findById(dto.leaveId());
        checkStatus(leave);
        checkDate(dto.startDate(), dto.endDate());
        Long employeeId = leave.getEmployeeId();
        Long tokenId = getDataFromResponse(orm.getEmployeeId(token));

        //Metod çalışanın kendisine özel, o yüzden bu kontrol.
        if (!employeeId.equals(tokenId))
            throw new HRException(ErrorType.UNAUTHORIZED);

        leave.setLeaveType(dto.leaveType());
        leave.setReason(dto.reason());
        leave.setStartDate(dto.startDate());
        leave.setEndDate(dto.endDate());
        leave.setUpdateAt(LocalDateTime.now());
        leaveRepository.save(leave);
        return true;
    }



    public Boolean deleteLeaveRequest(String token, Long leaveId) {
        Leave leave = findById(leaveId);
        checkCompany(token, leave.getEmployeeId());
        leave.setState(EState.PASSIVE);
        leaveRepository.save(leave);
        return true;
    }

    public Boolean cancelLeaveRequest(String token, Long leaveId) {
        Leave leave = findById(leaveId);
        Long tokenId = getDataFromResponse(orm.getEmployeeId(token));
        checkStatus(leave);
        if (!tokenId.equals(leave.getEmployeeId()))
            throw new HRException(ErrorType.UNAUTHORIZED);
        leave.setStatus(EStatus.CANCELLED);
        leaveRepository.save(leave);
        return true;
    }


    public Boolean approveLeaveRequest(String token, Long leaveId) {
        Leave leave = findById(leaveId);
        checkCompany(token, leave.getEmployeeId());
        Long managerId = getDataFromResponse(orm.getEmployeeId(token));
        leave.setApprovedBy(managerId);
        leave.setStatus(EStatus.APPROVED);
        leave.setUpdateAt(LocalDateTime.now());
        leaveRepository.save(leave);
        return true;
    }

    public Boolean rejectLeaveRequest(String token, Long leaveId) {
        Leave leave = findById(leaveId);
        checkCompany(token, leave.getEmployeeId());
        Long managerId = getDataFromResponse(orm.getEmployeeId(token));
        leave.setStatus(EStatus.REJECTED);
        leave.setApprovedBy(managerId);
        leave.setUpdateAt(LocalDateTime.now());
        leaveRepository.save(leave);
        return true;
    }

    private void checkCompany(String token, Long employeeId) {
        if (!getSuccessFromResponse(orm.checkCompanyId(token, employeeId)))
            throw new HRException(ErrorType.UNAUTHORIZED);
    }

    public Leave findById(Long leaveId) {
        return leaveRepository.findById(leaveId)
                .orElseThrow(() -> new HRException(ErrorType.LEAVE_NOT_FOUND));
    }

    private void checkDate(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate))
            throw new HRException(ErrorType.END_DATE_AFTER_START_DATE);
        if (startDate.isBefore(LocalDate.now()))
            throw new HRException(ErrorType.START_DATE_BEFORE_NOW);
    }

    private void checkStatus(Leave leave) {
        if (!leave.getStatus().equals(EStatus.PENDING))
            throw new HRException(ErrorType.LEAVE_ALREADY_APPROVED);
    }


}
