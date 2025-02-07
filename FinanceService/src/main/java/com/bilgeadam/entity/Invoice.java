package com.bilgeadam.entity;

import com.bilgeadam.entity.enums.EInvoiceStatus;
import feign.Param;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
//OKK
@EqualsAndHashCode(callSuper=true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="tbl_invoice") //FATURA
public class Invoice extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
