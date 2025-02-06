package com.bilgeadam.repository;

import com.bilgeadam.dto.response.PayrollResponse;
import com.bilgeadam.entity.Payroll;
import com.bilgeadam.entity.enums.EState;
import com.bilgeadam.entity.enums.EStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PayrollRepository extends JpaRepository<Payroll, Long> {

    @Query("SELECT NEW com.bilgeadam.dto.response.PayrollResponse(p.id,p.employeeId,p.salaryDate,p.grossSalary,p.deductions,p.netSalary,p.salaryCurrency,p.status) FROM Payroll p WHERE p.id = ?1")
    Optional<PayrollResponse> findPayrollResponseByPayrollId(Long payrollId);

    @Query("SELECT p FROM Payroll p WHERE p.employeeId IN (SELECT e.employeeId FROM EmployeeRecord e WHERE e.companyId = ?1) " +
            "AND p.state = com.bilgeadam.entity.enums.EState.ACTIVE ORDER BY p.createAt DESC")
    List<Payroll> findAllPayrollsByCompanyId(Long companyId);


    @Query("SELECT p FROM Payroll p WHERE p.employeeId IN (SELECT e.employeeId FROM EmployeeRecord e WHERE e.companyId = ?1) " +
            "AND p.state = com.bilgeadam.entity.enums.EState.ACTIVE AND YEAR(p.salaryDate) = ?2 AND MONTH(p.salaryDate)=?3  ORDER BY p.createAt DESC")
    List<Payroll> findAllPayrollsByCompanyIdAndYearAndMonth(Long companyId, int year, int month);

    @Query("SELECT p FROM Payroll p WHERE p.employeeId IN (SELECT e.employeeId FROM EmployeeRecord e WHERE e.companyId = ?1) " +
            "AND p.state = com.bilgeadam.entity.enums.EState.ACTIVE AND p.status = ?2 ORDER BY p.createAt DESC")
    List<Payroll> findAllPayrollsOnPending(Long companyId, EStatus status);

    List<Payroll> findAllByEmployeeIdAndStateOrderByCreateAtDesc(Long employeeId, EState state);



}
