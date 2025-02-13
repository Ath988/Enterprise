package com.bilgeadam.controller;

import com.bilgeadam.dto.request.PaymentUpdateRequestDTO;
import com.bilgeadam.dto.request.TaxRecordSaveRequestDTO;
import com.bilgeadam.dto.request.TaxRecordUpdateRequestDTO;
import com.bilgeadam.entity.Payment;
import com.bilgeadam.entity.TaxRecord;
import com.bilgeadam.entity.enums.EStatus;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.FinanceServiceException;
import com.bilgeadam.service.TaxRecordService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constant.RestApis.*;


@RestController
@RequestMapping(TAX_RECORD)
@RequiredArgsConstructor
@CrossOrigin("*")
public class TaxRecordController {


    private TaxRecordService taxRecordService;

    @PostMapping(SAVE_TAX_RECORD)
    public ResponseEntity<TaxRecord> createTaxRecord(@RequestBody TaxRecord taxRecord) {
        return ResponseEntity.ok(taxRecordService.createTaxRecord(taxRecord));
    }
    @DeleteMapping(DELETE_TAX_RECORD)
    @Operation(summary = "Odeme Silme")
    public ResponseEntity<Boolean> delete(Long id){

        return ResponseEntity.ok(taxRecordService.delete(id));
    }
    @Operation(summary = "Ödeme Güncelleme")
    @PutMapping(UPDATE_TAX_RECORD)
    public ResponseEntity<Boolean> update(@RequestBody TaxRecordUpdateRequestDTO dto){

        return ResponseEntity.ok(taxRecordService.update(dto));
    }
    @GetMapping(GET_ALL_TAX_RECORD)
    public ResponseEntity<List<TaxRecord>> getAllTaxRecords() {
        return ResponseEntity.ok(taxRecordService.getAllTaxRecords());
    }

    @GetMapping(GET_TAX_RECORD_BY_ID)
    public ResponseEntity<TaxRecord> getTaxRecordById(@PathVariable Long id) {
        return taxRecordService.getTaxRecordById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(GET_TAX_RECORD_BY_ACCOUNT_ID)
    public ResponseEntity<List<TaxRecord>> getTaxRecordsByAccountId(@PathVariable Long accountId) {
        return ResponseEntity.ok(taxRecordService.getTaxRecordsByAccountId(accountId));
    }

}
