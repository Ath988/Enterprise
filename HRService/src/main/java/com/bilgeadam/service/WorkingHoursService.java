package com.bilgeadam.service;

import com.bilgeadam.dto.request.AddWorkingHoursRequest;
import com.bilgeadam.dto.request.UpdateWorkingHoursRequest;
import com.bilgeadam.dto.response.WorkingHoursResponse;
import com.bilgeadam.entity.WorkingHours;
import com.bilgeadam.entity.enums.EState;
import com.bilgeadam.entity.enums.EStatus;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.HRException;
import com.bilgeadam.manager.OrganisationManagementManager;
import com.bilgeadam.repository.WorkingHoursRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.bilgeadam.dto.response.BaseResponse.*;

@Service
@RequiredArgsConstructor
public class WorkingHoursService {
    private final WorkingHoursRepository workingHoursRepository;
    private final EmployeeRecordService employeeRecordService;
    private final OrganisationManagementManager organisationManagementManager;

    public Boolean addWorkingHours(String token, AddWorkingHoursRequest dto) {
        if (!employeeRecordService.existsByEmployeeIdAndState(dto.employeeId()))
            throw new HRException(ErrorType.EMPLOYEE_RECORD_NOT_FOUND);
        checkCompany(token, dto.employeeId());

        workingHoursRepository.save(WorkingHours.builder()
                .employeeId(dto.employeeId())
                .date(dto.date())
                .startTime(dto.startTime())
                .endTime(dto.endTime())
                .status(EStatus.PENDING)
                .build());
        return true;
    }

    public List<WorkingHours> findAllWorkingHours(String token) {
        Long managerId = getDataFromResponse(organisationManagementManager.getEmployeeId(token));
        Long companyId = employeeRecordService.findCompanyIdByEmployeeId(managerId);
        return workingHoursRepository.findAllByCompanyId(companyId, EState.ACTIVE);
    }

    public List<WorkingHoursResponse> findAllWorkingHoursByEmployeeId(String token, Long employeeId) {
        checkCompany(token, employeeId);
        return workingHoursRepository.findAllByEmployeeId(employeeId, EState.ACTIVE);
    }

    public Boolean updateWorkingHours(String token, UpdateWorkingHoursRequest dto) {
        WorkingHours workingHours = findById(dto.id());
        checkCompany(token, workingHours.getEmployeeId());

        workingHours.setDate(dto.date());
        workingHours.setStartTime(dto.startTime());
        workingHours.setEndTime(dto.endTime());
        workingHours.setStatus(EStatus.PENDING);
        workingHours.setStatus(dto.status());
        workingHours.setUpdateAt(LocalDateTime.now());
        workingHoursRepository.save(workingHours);
        return true;
    }

    public Boolean deleteWorkingHours(String token, Long employeeId) {
        WorkingHours workingHours = workingHoursRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new HRException(ErrorType.WORKING_HOURS_NOT_FOUND));
        checkCompany(token, employeeId);
        workingHours.setState(EState.PASSIVE);
        workingHours.setUpdateAt(LocalDateTime.now());
        workingHoursRepository.save(workingHours);
        return true;
    }

    private void checkCompany(String token, Long employeeId) {
        if (!getSuccessFromResponse(organisationManagementManager.checkCompanyId(token, employeeId)))
            throw new HRException(ErrorType.UNAUTHORIZED);
    }

    public WorkingHours findById(Long employeeId) {
        return workingHoursRepository.findById(employeeId)
                .orElseThrow(() -> new HRException(ErrorType.WORKING_HOURS_NOT_FOUND));
    }
}
