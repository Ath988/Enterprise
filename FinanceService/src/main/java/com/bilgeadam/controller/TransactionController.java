package com.bilgeadam.controller;

import com.bilgeadam.dto.request.TransactionSaveRequestDTO;
import com.bilgeadam.dto.request.TransactionUpdateRequestDTO;
import com.bilgeadam.entity.Transaction;
import com.bilgeadam.entity.enums.EExpenseCategory;
import com.bilgeadam.entity.enums.ETransactionType;
import com.bilgeadam.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.bilgeadam.constant.RestApis.*;

@RestController
@RequestMapping(TRANSACTION)
@RequiredArgsConstructor
@CrossOrigin("*")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping(SAVE_TRANSACTION)
    public ResponseEntity<Boolean> save(@RequestBody TransactionSaveRequestDTO dto){

        return ResponseEntity.ok(transactionService.save(dto));
    }

    @DeleteMapping(DELETE_TRANSACTION)
    public ResponseEntity<Boolean> delete(Long id){

        return ResponseEntity.ok(transactionService.delete(id));
    }

    @PutMapping(UPDATE_TRANSACTION)
    public ResponseEntity<Boolean> update(@RequestBody TransactionUpdateRequestDTO dto){

        return ResponseEntity.ok(transactionService.update(dto));
    }

    @GetMapping(GET_TRANSACTION_BY_ACCOUNT_ID)
    @Operation(summary = "Hesap numarasına göre işlemleri getir")
    public ResponseEntity<List<Transaction>> getTransactionsByAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(transactionService.getTransactionsByAccountId(accountId));
    }

    @GetMapping(GET_TRANSACTION_BETWEEN_START_DATE_AND_END_DATE)
    @Operation(summary = "Belirtilen tarih aralığındaki işlemleri getir")
    public ResponseEntity<List<Transaction>> getTransactionsByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(transactionService.getTransactionsByDateRange(startDate, endDate));
    }

    @GetMapping(GET_TRANSACTION_BY_TYPE)
    @Operation(summary = "İşlem türüne (Gelir/Gider) göre kayıtları getir")
    public ResponseEntity<List<Transaction>> getTransactionsByType(@PathVariable ETransactionType type) {
        return ResponseEntity.ok(transactionService.getTransactionsByType(type));
    }

    @GetMapping(GET_TOTAL_INCOME)
    @Operation(summary = "Toplam gelir hesapla")
    public ResponseEntity<BigDecimal> getTotalIncome() {
        return ResponseEntity.ok(transactionService.getTotalIncome());
    }

    @GetMapping(GET_TOTAL_EXPENSE)
    @Operation(summary = "Toplam gider hesapla")
    public ResponseEntity<BigDecimal> getTotalExpense() {
        return ResponseEntity.ok(transactionService.getTotalExpense());
    }

    @GetMapping(GET_NET_PROFIT)
    @Operation(summary = "Net kar hesapla (Gelir - Gider)")
    public ResponseEntity<BigDecimal> getNetProfit() {
        return ResponseEntity.ok(transactionService.getNetProfit());
    }

    @GetMapping(GET_TRANSACTION_BY_CATEGORY)
    @Operation(summary = "Gider kategorisine göre sıralı işlemleri getir")
    public ResponseEntity<List<Transaction>> getSortedTransactionsByCategory(
            @RequestParam ETransactionType type,
            @RequestParam EExpenseCategory category,
            @RequestParam boolean ascending) {
        return ResponseEntity.ok(transactionService.getSortedTransactionsByCategory(type, category, ascending));
    }

}
