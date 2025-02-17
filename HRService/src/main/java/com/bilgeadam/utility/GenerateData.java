package com.bilgeadam.utility;

import com.bilgeadam.entity.EmployeeRecord;
import com.bilgeadam.entity.Leave;
import com.bilgeadam.entity.Payroll;
import com.bilgeadam.entity.enums.ECurrency;
import com.bilgeadam.entity.enums.ELeaveType;
import com.bilgeadam.entity.enums.EStatus;
import com.bilgeadam.repository.EmployeeRecordRepository;
import com.bilgeadam.repository.LeaveRepository;
import com.bilgeadam.repository.PayrollRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class GenerateData {
    private final EmployeeRecordRepository employeeRecordRepository;
    private final LeaveRepository leaveRepository;
    private final PayrollRepository payrollRepository;

    @PostConstruct
    public void generateData(){
        if(employeeRecordRepository.count() == 0){
            EmployeeRecord record1 = EmployeeRecord.builder().employeeId(1L).companyId(1L).startDate(LocalDate.now()).build();
            EmployeeRecord record2 = EmployeeRecord.builder().employeeId(2L).companyId(1L).startDate(LocalDate.now()).build();
            EmployeeRecord record3 = EmployeeRecord.builder().employeeId(3L).companyId(1L).startDate(LocalDate.now()).build();
            EmployeeRecord record4 = EmployeeRecord.builder().employeeId(4L).companyId(1L).startDate(LocalDate.now()).build();
            EmployeeRecord record5 = EmployeeRecord.builder().employeeId(5L).companyId(1L).startDate(LocalDate.now()).build();
            employeeRecordRepository.saveAll(List.of(record1, record2, record3,record4,record5));
        }

        if(leaveRepository.count() == 0){
            Leave leave1 = Leave.builder().employeeId(4L).startDate(LocalDate.now()).endDate(LocalDate.now().plusDays(2)).status(EStatus.PENDING).leaveType(ELeaveType.UCRETSIZ_IZIN).duration(2).reason("Özel sebeplerden 2 gün izin istiyorum.").build();
            Leave leave2 = Leave.builder().employeeId(5L).startDate(LocalDate.now()).endDate(LocalDate.now().plusDays(3)).status(EStatus.PENDING).leaveType(ELeaveType.UCRETSIZ_IZIN).duration(2).reason("Hastalandım.").build();
            Leave leave3 = Leave.builder().employeeId(3L).startDate(LocalDate.now()).endDate(LocalDate.now().plusDays(5)).status(EStatus.PENDING).leaveType(ELeaveType.YILLIK_IZIN).duration(2).reason("Yıllık iznimi kullanmak istiyorum.").build();
            leaveRepository.saveAll(List.of(leave1, leave2, leave3));
        }

        if (payrollRepository.count() == 0) {
            Payroll payroll1 = Payroll.builder().status(EStatus.PENDING).employeeId(2L).salaryCurrency(ECurrency.USD)
                    .grossSalary(3500.0).deductions(250.0).netSalary(3250.0).salaryDate(LocalDate.of(2025,2,10)).build();
            Payroll payroll2 = Payroll.builder().status(EStatus.PENDING).employeeId(3L).salaryCurrency(ECurrency.TRY)
                    .grossSalary(45000.0).deductions(5500.0).netSalary(39500.0).salaryDate(LocalDate.of(2025,2,10)).build();
            Payroll payroll3 = Payroll.builder().status(EStatus.PENDING).employeeId(4L).salaryCurrency(ECurrency.TRY)
                    .grossSalary(65000.0).deductions(7500.0).netSalary(57500.0).salaryDate(LocalDate.of(2025,2,10)).build();
            Payroll payroll4 = Payroll.builder().status(EStatus.PENDING).employeeId(5L).salaryCurrency(ECurrency.TRY)
                    .grossSalary(125000.0).deductions(8500.0).netSalary(116500.0).salaryDate(LocalDate.of(2025,2,10)).build();

            Payroll payroll5 = Payroll.builder().status(EStatus.PENDING).employeeId(2L).salaryCurrency(ECurrency.USD)
                    .grossSalary(3500.0).deductions(250.0).netSalary(3250.0).salaryDate(LocalDate.of(2025,1,10)).build();
            Payroll payroll6 = Payroll.builder().status(EStatus.PENDING).employeeId(3L).salaryCurrency(ECurrency.TRY)
                    .grossSalary(45000.0).deductions(5500.0).netSalary(39500.0).salaryDate(LocalDate.of(2025,1,10)).build();
            Payroll payroll7 = Payroll.builder().status(EStatus.PENDING).employeeId(4L).salaryCurrency(ECurrency.TRY)
                    .grossSalary(65000.0).deductions(7500.0).netSalary(57500.0).salaryDate(LocalDate.of(2025,1,10)).build();
            Payroll payroll8 = Payroll.builder().status(EStatus.PENDING).employeeId(5L).salaryCurrency(ECurrency.TRY)
                    .grossSalary(125000.0).deductions(8500.0).netSalary(116500.0).salaryDate(LocalDate.of(2025,1,10)).build();

            payrollRepository.saveAll(List.of(payroll1,payroll2,payroll3,payroll4,payroll5,payroll6,payroll7,payroll8));
        }

    }
}
