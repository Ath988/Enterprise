package com.bilgeadam.service;

import com.bilgeadam.dto.request.AddPayrollRequest;
import com.bilgeadam.dto.request.UpdatePayrollRequest;
import com.bilgeadam.dto.response.PayrollResponse;
import com.bilgeadam.entity.Payroll;
import com.bilgeadam.entity.enums.EState;
import com.bilgeadam.entity.enums.EStatus;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.HRException;
import com.bilgeadam.manager.OrganisationManagementManager;
import com.bilgeadam.repository.PayrollRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


import static com.bilgeadam.dto.response.BaseResponse.*;

@Service
@RequiredArgsConstructor
public class PayrollService {
    private final PayrollRepository payrollRepository;
    private final OrganisationManagementManager organisationManagementManager;
    private final EmployeeRecordService employeeRecordService;

    @Transactional
    public Boolean addPayroll(String token, AddPayrollRequest dto) {
        if (!employeeRecordService.existsByEmployeeIdAndState(dto.employeeId()))
            throw new HRException(ErrorType.EMPLOYEE_RECORD_NOT_FOUND);

        checkCompany(token,dto.employeeId());

        payrollRepository.save(Payroll.builder()
                .employeeId(dto.employeeId())
                .grossSalary(dto.grossSalary())
                .deductions(dto.deductions())
                .netSalary(dto.netSalary())
                .salaryCurrency(dto.currency())
                .salaryDate(dto.payDate())
                .status(EStatus.PENDING)
                .build());
        return true;
    }

    public PayrollResponse getPayroll(String token, Long payrollId) {
        PayrollResponse payrollResponse = payrollRepository.findPayrollResponseByPayrollId(payrollId)
                .orElseThrow(() -> new HRException(ErrorType.PAYROLL_NOT_FOUND));
        Long employeeId = payrollResponse.getEmployeeId();
        checkCompany(token, employeeId);

        String employeeName = getDataFromResponse(organisationManagementManager.getEmployeeNameByToken(token, Optional.of(employeeId)));
        payrollResponse.setEmployeeName(employeeName);
        return payrollResponse;
    }


    //Todo: Pagination
    public List<PayrollResponse> getAllPayrolls(String token) {
        Long managerId = getDataFromResponse(organisationManagementManager.getEmployeeId(token));
        Long companyId = employeeRecordService.findCompanyIdByEmployeeId(managerId);

        List<Payroll> payrollList = payrollRepository.findAllPayrollsByCompanyId(companyId);


        Set<Long> employeeIds = payrollList.stream()
                .map(Payroll::getEmployeeId)
                .collect(Collectors.toSet());

        Map<Long, String> employeeNameMap
                = getDataFromResponse(organisationManagementManager.getAllEmployeeNames(new ArrayList<>(employeeIds)));

        return mapPayrollsToResponse(payrollList, employeeNameMap);
    }

    public List<PayrollResponse> getAllPayrollsByYearAndMonth(String token, int year, int month) {
        Long managerId = getDataFromResponse(organisationManagementManager.getEmployeeId(token));
        Long companyId = employeeRecordService.findCompanyIdByEmployeeId(managerId);

        List<Payroll> payrollList = payrollRepository.findAllPayrollsByCompanyIdAndYearAndMonth(companyId, year, month);
        return getPayrollResponsesOnCriteria(payrollList);
    }

    public List<PayrollResponse> getAllPendingPayrolls(String token) {
        Long managerId = getDataFromResponse(organisationManagementManager.getEmployeeId(token));
        Long companyId = employeeRecordService.findCompanyIdByEmployeeId(managerId);
        List<Payroll> payrollList = payrollRepository.findAllPayrollsOnPending(companyId, EStatus.PENDING);
        return getPayrollResponsesOnCriteria(payrollList);
    }

    //Burada direk Payroll dondum cunku calisan ismini responseta gostermeye gerek yok.
    public List<Payroll> findAllPayrollsByEmployeeId(String token, Long employeeId) {
        checkCompany(token, employeeId);
        return payrollRepository.findAllByEmployeeIdAndStateOrderByCreateAtDesc(employeeId, EState.ACTIVE);
    }


    @Transactional
    public Boolean updatePayroll(String token, UpdatePayrollRequest dto) {
        Payroll payroll = findById(dto.payrollId());
        checkCompany(token, payroll.getEmployeeId());
        payroll.setSalaryDate(dto.payDate());
        payroll.setGrossSalary(dto.grossSalary());
        payroll.setNetSalary(dto.netSalary());
        payroll.setDeductions(dto.deductions());
        payroll.setSalaryCurrency(dto.currency());
        payroll.setStatus(EStatus.PENDING);
        payroll.setUpdateAt(LocalDateTime.now());
        payrollRepository.save(payroll);
        return true;
    }

    @Transactional
    public Boolean deletePayroll(String token, Long payrollId) {
        Payroll payroll = findById(payrollId);
        checkCompany(token, payroll.getEmployeeId());

        payroll.setUpdateAt(LocalDateTime.now());
        payroll.setState(EState.PASSIVE);
        payrollRepository.save(payroll);
        return true;
    }

    public Boolean approvePayroll(String token, Long payrollId) {
        Payroll payroll = findById(payrollId);
        checkCompany(token, payroll.getEmployeeId());

        payroll.setStatus(EStatus.APPROVED);
        payroll.setUpdateAt(LocalDateTime.now());
        payrollRepository.save(payroll);
        return true;
    }

    public Boolean rejectPayroll(String token, Long payrollId) {
        Payroll payroll = findById(payrollId);
        checkCompany(token, payroll.getEmployeeId());

        payroll.setStatus(EStatus.REJECTED);
        payroll.setUpdateAt(LocalDateTime.now());
        payrollRepository.save(payroll);
        return true;
    }


    public Payroll findById(Long payrollId) {
        return payrollRepository.findById(payrollId)
                .orElseThrow(() -> new HRException(ErrorType.PAYROLL_NOT_FOUND));
    }

    private List<PayrollResponse> mapPayrollsToResponse(List<Payroll> payrollList, Map<Long, String> employeeNameMap) {
        return payrollList.stream()
                .map(p -> PayrollResponse.builder()
                        .id(p.getId())
                        .employeeId(p.getEmployeeId())
                        .employeeName(employeeNameMap.getOrDefault(p.getEmployeeId(), "UNKNOWN"))
                        .grossSalary(p.getGrossSalary())
                        .deductions(p.getDeductions())
                        .netSalary(p.getNetSalary())
                        .salaryDate(p.getSalaryDate())
                        .status(p.getStatus())
                        .currency(p.getSalaryCurrency())
                        .build())
                .toList();
    }

    private List<PayrollResponse> getPayrollResponsesOnCriteria(List<Payroll> payrollList) {
        if (payrollList.isEmpty()) return Collections.emptyList();

        Set<Long> employeeIds = payrollList.stream()
                .map(Payroll::getEmployeeId)
                .collect(Collectors.toSet());

        Map<Long, String> employeeNameMap = getDataFromResponse(organisationManagementManager.getAllEmployeeNames(new ArrayList<>(employeeIds)));
        return mapPayrollsToResponse(payrollList, employeeNameMap);
    }

    private void checkCompany(String token, Long employeeId) {
        if (!getSuccessFromResponse(organisationManagementManager.checkCompanyId(token, employeeId)))
            throw new HRException(ErrorType.UNAUTHORIZED);
    }


}
