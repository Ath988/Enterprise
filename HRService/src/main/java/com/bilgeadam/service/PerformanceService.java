package com.bilgeadam.service;

import com.bilgeadam.dto.request.AddPerformanceRequest;
import com.bilgeadam.dto.request.UpdatePerformanceRequest;
import com.bilgeadam.dto.response.PerformanceResponse;
import com.bilgeadam.entity.EmployeeRecord;
import com.bilgeadam.entity.Performance;
import com.bilgeadam.entity.enums.EState;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.HRException;
import com.bilgeadam.manager.OrganisationManagementManager;
import com.bilgeadam.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    //Todo: Pagination
    public List<PerformanceResponse> getAllPerformances(String token) {
        Long managerId = getDataFromResponse(organisationManagementManager.getEmployeeId(token));
        Long companyId = employeeRecordService.findCompanyIdByEmployeeId(managerId);


        List<Performance> performances = performanceRepository.findAllPerformancesByCompanyId(companyId);

        Set<Long> employeeIds = performances.stream()
                .flatMap(p -> Stream.of(p.getEmployeeId(), p.getEvaulatedBy()))
                .collect(Collectors.toSet());

        Map<Long, String> employeeNameMap = getDataFromResponse(organisationManagementManager.getAllEmployeeNames(new ArrayList<>(employeeIds)));

        return performances.stream()
                .map(p -> PerformanceResponse.builder()
                        .id(p.getId())
                        .employeeId(p.getEvaulatedBy())
                        .employeeName(employeeNameMap.get(p.getEmployeeId()))
                        .evaulatedBy(employeeNameMap.get(p.getEvaulatedBy()))
                        .grade(p.getGrade())
                        .feedback(p.getFeedBack())
                        .trainingHours(p.getTrainingHours())
                        .trainingTopics(p.getTrainingTopics())
                        .evaluationDate(p.getCreateAt())
                        .build())
                .toList();
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

    public Boolean updatePerformance(String token, UpdatePerformanceRequest dto){
        Performance performance = findById(dto.performanceId());
        if(getSuccessFromResponse(organisationManagementManager.checkCompanyId(token, performance.getEmployeeId()))){
            performance.setGrade(dto.grade());
            performance.setTrainingHours(dto.trainingHours());
            performance.setTrainingTopics(dto.trainingTopics());
            performance.setFeedBack(dto.feedback());
            performance.setUpdateAt(LocalDateTime.now());
            performanceRepository.save(performance);
            return true;
        }
        throw new HRException(ErrorType.UNAUTHORIZED);
    }

    public Boolean deletePerformance(String token, Long performanceId){
        Performance performance = findById(performanceId);
        if(getSuccessFromResponse(organisationManagementManager.checkCompanyId(token,performance.getEmployeeId()))){
            performance.setUpdateAt(LocalDateTime.now());
            performance.setState(EState.PASSIVE);
            performanceRepository.save(performance);
            return true;
        }
        throw new HRException(ErrorType.UNAUTHORIZED);
    }

    public Performance findById(Long performanceId){
        return performanceRepository.findById(performanceId)
                .orElseThrow(()->new HRException(ErrorType.PERFORMANCE_NOT_FOUND));
    }

}
