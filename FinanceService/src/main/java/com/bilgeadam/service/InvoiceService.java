package com.bilgeadam.service;

import com.bilgeadam.dto.request.InvoiceSaveRequestDTO;
import com.bilgeadam.dto.request.InvoiceUpdateRequestDTO;
import com.bilgeadam.dto.request.PageRequestDTO;
import com.bilgeadam.entity.Invoice;
import com.bilgeadam.entity.enums.EStatus;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.FinanceServiceException;
import com.bilgeadam.repository.InvoiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;

    public Boolean save(InvoiceSaveRequestDTO dto) {
        Invoice invoice = Invoice.builder()
                .taxNo(dto.taxNo())
                .companyName(dto.companyName())
                .companyAdress(dto.companyAdress())
                .buyerEmail(dto.buyerEmail())
                .buyerPhone(dto.buyerPhone())
                .productId(dto.productId())
                .productName(dto.productName())
                .pieces(dto.pieces())
                .invoiceDate(dto.invoiceDate())
                .price(dto.price())
                .totalAmount(dto.totalAmount())
                .build();

        invoiceRepository.save(invoice);
        return true;
    }

    public Boolean update(InvoiceUpdateRequestDTO dto) {
        Invoice invoice = invoiceRepository.findById(dto.id()).orElseThrow(() -> new FinanceServiceException(ErrorType.INVOICE_NOT_FOUND));
        invoice.setTaxNo(dto.taxNo());
        invoice.setCompanyName(dto.companyName());
        invoice.setCompanyAdress(dto.companyAdress());
        invoice.setBuyerEmail(dto.buyerEmail());
        invoice.setBuyerPhone(dto.buyerPhone());
        invoice.setProductId(dto.productId());
        invoice.setProductName(dto.productName());
        invoice.setPieces(dto.pieces());
        invoice.setInvoiceDate(dto.invoiceDate());
        invoice.setTotalAmount(dto.totalAmount());
        invoiceRepository.save(invoice);
        return true;
    }

    public Boolean delete(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(() -> new FinanceServiceException(ErrorType.INVOICE_NOT_FOUND));
        invoice.setStatus(EStatus.DELETED);
        invoiceRepository.save(invoice);
        return true;
    }

    public Invoice findById(Long id) {
        return invoiceRepository.findById(id).orElseThrow(() -> new FinanceServiceException(ErrorType.INVOICE_NOT_FOUND));
    }

    public List<Invoice> findAll(PageRequestDTO dto) {

        return invoiceRepository.findAllByStatusNot(EStatus.DELETED, PageRequest.of(dto.page(), dto.size())).getContent();
    }

    public Invoice findByIdAndInvoiceDate(Long id, LocalDate invoiceDate) {
        return invoiceRepository.findByIdAndInvoiceDate(id, invoiceDate);
    }
}
