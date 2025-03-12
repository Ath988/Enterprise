package com.bilgeadam.repository;


import com.bilgeadam.entity.Account;
import com.bilgeadam.entity.FinancialReport;
import com.bilgeadam.entity.enums.EStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FinancialReportRepository extends JpaRepository<FinancialReport, Long> {

    List<FinancialReport> findByAccountId(Long accountId);

    List<FinancialReport> findByAccountIdAndStartDateBetween(Long accountId, LocalDate start, LocalDate end);

    Page<FinancialReport> findAllByStatusNot(EStatus status, Pageable pageable);


}
