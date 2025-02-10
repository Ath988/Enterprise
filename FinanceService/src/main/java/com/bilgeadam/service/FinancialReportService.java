package com.bilgeadam.service;

import com.bilgeadam.dto.request.FinancialReportUpdateRequestDTO;
import com.bilgeadam.dto.request.PageRequestDTO;
import com.bilgeadam.entity.Account;
import com.bilgeadam.entity.FinancialReport;
import com.bilgeadam.entity.enums.EStatus;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.FinanceServiceException;
import com.bilgeadam.repository.FinancialReportRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FinancialReportService {

    private FinancialReportRepository financialReportRepository;

    public FinancialReport save(FinancialReport report) {

        return financialReportRepository.save(report);
    }

    public Boolean deleteFinancialReport(Long id) {
        FinancialReport financialReport = financialReportRepository.findById(id).orElseThrow(() -> new FinanceServiceException(ErrorType.FINANCIAL_REPORT_NOT_FOUND));
        financialReport.setStatus(EStatus.DELETED);
        financialReportRepository.save(financialReport);
        return true;
    }

    public Boolean update(FinancialReportUpdateRequestDTO dto) {
        FinancialReport financialReport = financialReportRepository.findById(dto.id()).orElseThrow(() -> new FinanceServiceException(ErrorType.FINANCIAL_REPORT_NOT_FOUND));
        financialReport.setAccountId(dto.accountId());
        financialReport.setStartDate(dto.startDate());
        financialReport.setEndDate(dto.endDate());
        financialReport.setTotalIncome(dto.totalIncome());
        financialReport.setTotalExpense(dto.totalExpense());
        financialReport.setNetProfit(dto.netProfit());
        financialReport.setMessage(dto.message());
        financialReportRepository.save(financialReport);
        return true;
    }

    public List<FinancialReport> findByAccountId(Long accountId) {
        return financialReportRepository.findByAccountId(accountId);
    }

    public List<FinancialReport> findAll(PageRequestDTO dto) {
        return financialReportRepository.findAllByStatusNot(EStatus.DELETED, PageRequest.of(dto.page(), dto.size())).getContent();
    }

    public List<FinancialReport> findByDateRange(Long accountId, LocalDate start, LocalDate end) {
        return financialReportRepository.findByAccountIdAndStartDateBetween(accountId, start, end);
    }


    public FinancialReport findById(Long id) {
        return financialReportRepository.findById(id).orElseThrow(() -> new FinanceServiceException(ErrorType.ACCOUNT_NOT_FOUND));
    }
}

