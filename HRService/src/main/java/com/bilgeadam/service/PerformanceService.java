package com.bilgeadam.service;

import com.bilgeadam.dto.request.AddPerformanceRequest;
import com.bilgeadam.dto.response.PerformanceResponse;
import com.bilgeadam.dto.response.otherServices.EmployeeDetailResponse;
import com.bilgeadam.dto.response.otherServices.EmployeeSaveResponse;
import com.bilgeadam.entity.EmployeeRecord;
import com.bilgeadam.entity.Performance;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.HRException;
import com.bilgeadam.manager.OrganisationManagementManager;
import com.bilgeadam.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.bilgeadam.dto.response.BaseResponse.*;

@Service
@RequiredArgsConstructor
public class PerformanceService {
    private final PerformanceRepository performanceRepository;
    private final OrganisationManagementManager organisationManagementManager;
    private final EmployeeRecordService employeeRecordService;

    public PerformanceResponse getPerformance(String token,Long performanceId){
        Long employeeId = performanceRepository.findEmployeeIdByPerformanceId(performanceId)
                .orElseThrow(()->new HRException(ErrorType.PERFORMANCE_NOT_FOUND));
        PerformanceResponse performanceResponse = performanceRepository.findPerformanceResponseByPerformanceId(performanceId)
                .orElseThrow(() -> new HRException(ErrorType.PERFORMANCE_NOT_FOUND));

        performanceResponse.setEmployeeName(getDataFromResponse(organisationManagementManager.getEmployeeNameByToken(token, Optional.of(employeeId))));
        performanceResponse.setEvaulatedBy(getDataFromResponse(organisationManagementManager.getEmployeeNameByToken(token,Optional.empty())));

        return performanceResponse;
    }

    public Boolean addPerformance(String token, AddPerformanceRequest dto){
        EmployeeRecord employeeRecord = employeeRecordService.findById(dto.employeeRecordId());
        if(getSuccessFromResponse(organisationManagementManager.checkCompanyId(token, employeeRecord.getEmployeeId()))){
            Long managerId = getDataFromResponse(organisationManagementManager.getEmployeeId(token));
            Performance performance = Performance.builder()
                    .employeeId(employeeRecord.getEmployeeId())
                    .evaulatedBy(managerId)
                    .feedBack(dto.feedback())
                    .grade(dto.grade())
                    .trainingHours(dto.trainingHours())
                    .trainingTopics(dto.trainingTopics())
                    .build();
            performanceRepository.save(performance);
            return true;
        }
        throw new HRException(ErrorType.UNAUTHORIZED);
    }

}
