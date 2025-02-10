package com.bilgeadam.RabbitMQ;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class InvoiceModel {
    String taxNo;            // Vergi Numarası ya da TC numarası da girilebilir.
    String companyName;      // Şirket Adı
    String companyAdress;    // Şirket  Adres Bilgisi
    String buyerEmail;       // Alıcı Email Bilgisi
    String buyerPhone;       // Alıcı Telefon Bilgisi
    Long productId;          // Satılan ürün/hizmet ID
    String productName;      // Ürün Adı
    Integer pieces;          // Ürün Adet
    LocalDate invoiceDate;   // Fatura Tarih
    BigDecimal price;        // Birim Fiyat
    BigDecimal totalAmount;  // Toplam Satıs Tutarı
}