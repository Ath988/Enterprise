package com.bilgeadam.repository;
import com.bilgeadam.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByAccountId(Long accountId);
    List<Payment> findByInvoiceId(Long invoiceId);
    List<Payment> findByIsPaid(Boolean isPaid);
}
