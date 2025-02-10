package com.bilgeadam.controller;

import com.bilgeadam.dto.request.*;
import com.bilgeadam.entity.Account;
import com.bilgeadam.entity.Invoice;
import com.bilgeadam.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.bilgeadam.constant.RestApis.*;

@RestController
@RequestMapping(INVOICE)
@RequiredArgsConstructor
@CrossOrigin("*")
public class InvoiceController {
    private final InvoiceService invoiceService;

    @PostMapping(SAVE)
    @Operation(summary = "Yeni Fatura Oluşturma")
    public ResponseEntity<Boolean> save(@RequestBody InvoiceSaveRequestDTO dto) {
        return ResponseEntity.ok(invoiceService.save(dto));
    }
    @DeleteMapping(DELETE)
    @Operation(summary = "Fatura Silme")
    public ResponseEntity<Boolean> delete(Long id){

        return ResponseEntity.ok(invoiceService.delete(id));
    }
    @Operation(summary = "Fatura Güncelleme")
    @PutMapping(UPDATE)
    public ResponseEntity<Boolean> update(@RequestBody InvoiceUpdateRequestDTO dto){

        return ResponseEntity.ok(invoiceService.update(dto));
    }
    @Operation(summary = "Tüm Faturalar")
    @GetMapping(FIND_ALL)
    public ResponseEntity<List<Invoice>> findAll(@RequestBody PageRequestDTO dto){

        return ResponseEntity.ok(invoiceService.findAll(dto));
    }
    @Operation(summary = "ID'si Verilen Faturayı Bulma")
    @GetMapping(FIND_BY_ID)
    public ResponseEntity<Invoice> findById(Long id){
        return ResponseEntity.ok(invoiceService.findById(id));
    }

    @Operation(summary = "Fatura Tarihine Göre Kesilen Faturaları Bulma")
    @GetMapping(FIND_BY_ALL_INVOICE_DATE)
    public ResponseEntity<Invoice> findByIdAndInvoiceDate(Long id, LocalDate invoiceDate){
        return ResponseEntity.ok(invoiceService.findByIdAndInvoiceDate(id,invoiceDate));
    }
}
