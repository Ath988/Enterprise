package com.bilgeadam.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InvoiceUpdateRequestDTO(
        Long id,
        String taxNo,            // Vergi Numarası ya da TC numarası da girilebilir.
        Long companyId,
        String buyerEmail,       // Alıcı Email Bilgisi
        String buyerPhone,       // Alıcı Telefon Bilgisi
        Long productId,          // Satılan ürün/hizmet ID
        String productName,      // Ürün Adı
        Integer pieces,          // Ürün Adet
        LocalDate invoiceDate,   // Fatura Tarih
        BigDecimal price,        // Birim Fiyat
        BigDecimal totalAmount) {
}
