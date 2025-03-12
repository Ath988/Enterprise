package com.bilgeadam.repository;
import com.bilgeadam.entity.Invoice;
import com.bilgeadam.entity.enums.EStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Page<Invoice> findAllByStatusNot(EStatus status, Pageable pageable);

    Invoice findByIdAndInvoiceDate(Long id, LocalDate invoiceDate);
}
