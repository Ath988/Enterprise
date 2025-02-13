package com.bilgeadam.controller;

import com.bilgeadam.dto.request.InvoiceUpdateRequestDTO;
import com.bilgeadam.dto.request.PaymentSaveRequestDTO;
import com.bilgeadam.dto.request.PaymentUpdateRequestDTO;
import com.bilgeadam.entity.Invoice;
import com.bilgeadam.entity.Payment;
import com.bilgeadam.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.bilgeadam.constant.RestApis.*;


@RestController
@RequestMapping(PAYMENT)
@RequiredArgsConstructor
@CrossOrigin("*")
public class PaymentController {

    private PaymentService paymentService;


    @PostMapping(SAVE_PAYMENT)
    @Operation(summary = "Yeni Odeme Oluşturma")
    public ResponseEntity<Boolean> save(@RequestBody PaymentSaveRequestDTO dto) {
        return ResponseEntity.ok(paymentService.save(dto));
    }
    @DeleteMapping(DELETE_PAYMENT)
    @Operation(summary = "Odeme Silme")
    public ResponseEntity<Boolean> delete(Long id){

        return ResponseEntity.ok(paymentService.delete(id));
    }
    @Operation(summary = "Ödeme Güncelleme")
    @PutMapping(UPDATE_PAYMENT)
    public ResponseEntity<Boolean> update(@RequestBody PaymentUpdateRequestDTO dto){

        return ResponseEntity.ok(paymentService.update(dto));
    }
    @Operation(summary = "ID'si Verilen Faturayı Bulma")
    @GetMapping(GET_PAYMENT_BY_ID)
    public ResponseEntity<Payment> findById(Long id){
        return ResponseEntity.ok(paymentService.findById(id));
    }

    @GetMapping(GET_PAYMENT_BY_ACCOUNT_ID)
    @Operation(summary = "Hesap Numarası Verilen Ödemeleri Listeleme")
    public ResponseEntity<List<Payment>> getPaymentsByAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(paymentService.findByAccountId(accountId));
    }


    @GetMapping(GET_PAYMENT_BY_INVOICE_ID)
    @Operation(summary = "Fatura Numarası Verilen Ödemeleri Listeleme")
    public ResponseEntity<List<Payment>> getPaymentsByInvoice(@PathVariable Long invoiceId) {
        return ResponseEntity.ok(paymentService.findByInvoiceId(invoiceId));
    }

    @GetMapping(GET_PAYMENT_BY_STATUS_IS_PAID)
    @Operation(summary = "Ödemesi Yapılanlar")
    public ResponseEntity<List<Payment>> getPaymentsByStatus(@RequestParam Boolean isPaid) {
        return ResponseEntity.ok(paymentService.findByPaymentStatus(isPaid));
    }
}
