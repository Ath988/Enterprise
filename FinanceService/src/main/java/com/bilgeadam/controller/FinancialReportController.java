package com.bilgeadam.controller;

import com.bilgeadam.dto.request.FinancialReportUpdateRequestDTO;
import com.bilgeadam.dto.request.PageRequestDTO;
import com.bilgeadam.dto.response.ResponseDTO;
import com.bilgeadam.entity.FinancialReport;
import com.bilgeadam.service.FinancialReportService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.bilgeadam.constant.RestApis.*;

@RestController
@RequestMapping(FINANCIAL_REPORT)
@RequiredArgsConstructor
@CrossOrigin("*")
public class FinancialReportController {

    private FinancialReportService financialReportService;

    @PostMapping(SAVE_FINANCIAL_REPORT)
    @Operation(summary = "Yeni Finansal Rapor Oluşturma")
    public ResponseEntity<FinancialReport> createReport(@RequestBody FinancialReport report) {
        return ResponseEntity.ok(financialReportService.save(report));
    }

    @DeleteMapping(DELETE_FINANCIAL_REPORT)
    public ResponseEntity<Boolean> delete(Long id) {
        return ResponseEntity.ok(financialReportService.deleteFinancialReport(id));
    }

    @PutMapping(UPDATE_FINANCIAL_REPORT)
    public ResponseEntity<Boolean> update(@RequestBody FinancialReportUpdateRequestDTO dto) {
        return ResponseEntity.ok(financialReportService.update(dto));
    }

    @GetMapping(GET_ALL_FINANCIAL_REPORT)
    public ResponseEntity<ResponseDTO<List<FinancialReport>>> findAll(@RequestBody PageRequestDTO dto) {
        return ResponseEntity.ok(ResponseDTO
                .<List<FinancialReport>>builder()
                .data(financialReportService.findAll(dto))
                .message("Success")
                .code(200)
                .build());
    }

    @GetMapping(GET_FINANCIAL_REPORT_BY_ID)
    public ResponseEntity<FinancialReport> findById(Long id){
        return ResponseEntity.ok(financialReportService.findById(id));
    }
    @GetMapping(FIND_BY_ACCOUNT_ID_BETWEEN_DATE)
    @Operation(summary = "Account ID'sine Göre İki Tarih Arasında Finansal Rapor Olusturma")
    public ResponseEntity<List<FinancialReport>> getReportsByDateRange(
            @PathVariable Long accountId,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        return ResponseEntity.ok(financialReportService.findByDateRange(accountId, start, end));
    }

    @GetMapping(FIND_BY_ACCOUNT_ID)
    @Operation(summary = "Account ID'sine Göre Finansal Rapor Olusturma")
    public ResponseEntity<List<FinancialReport>> getReportsByAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(financialReportService.findByAccountId(accountId));
    }


}
