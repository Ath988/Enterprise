package com.bilgeadam.service;

import com.bilgeadam.dto.request.TaxRecordUpdateRequestDTO;
import com.bilgeadam.entity.Account;
import com.bilgeadam.entity.Payment;
import com.bilgeadam.entity.TaxRecord;
import com.bilgeadam.entity.enums.EStatus;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.FinanceServiceException;
import com.bilgeadam.repository.TaxRecordRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TaxRecordService {

    private TaxRecordRepository taxRecordRepository;

    public TaxRecord createTaxRecord(TaxRecord taxRecord) {
        return taxRecordRepository.save(taxRecord);
    }

    public List<TaxRecord> getAllTaxRecords() {
        return taxRecordRepository.findAll();
    }

    public Optional<TaxRecord> getTaxRecordById(Long id) {
        return taxRecordRepository.findById(id);
    }

    public List<TaxRecord> getTaxRecordsByAccountId(Long accountId) {
        return taxRecordRepository.findByAccountId(accountId);
    }

    public Boolean update(TaxRecordUpdateRequestDTO dto) {
        TaxRecord taxRecord = taxRecordRepository.findById(dto.id()).orElseThrow(() -> new FinanceServiceException(ErrorType.TAX_RECORD_NOT_FOUND));
        taxRecord.setAccountId(dto.accountId());
        taxRecord.setTaxType(dto.taxType());
        taxRecord.setTaxAmount(dto.taxAmount());
        taxRecord.setTaxPeriodStart(dto.taxPeriodStart());
        taxRecord.setTaxPeriodEnd(dto.taxPeriodEnd());
        taxRecord.setPaymentDate(dto.paymentDate());
        taxRecord.setTaxRate(dto.taxRate());
        taxRecord.setDescription(dto.description());
        return true;
    }

    public Boolean delete(Long id) {
        TaxRecord taxRecord = taxRecordRepository.findById(id).orElseThrow(() -> new FinanceServiceException(ErrorType.TAX_RECORD_NOT_FOUND));
        taxRecord.setStatus(EStatus.DELETED);
        taxRecordRepository.save(taxRecord);
        return true;
    }

    public List<TaxRecord> getTaxRecordsByPeriod(LocalDate startDate, LocalDate endDate) {
        return taxRecordRepository.findAll().stream()
                .filter(record -> !record.getTaxPeriodStart().isBefore(startDate) &&
                        !record.getTaxPeriodEnd().isAfter(endDate))
                .toList();
    }


}
