package com.bilgeadam.service;

import com.bilgeadam.dto.request.PaymentSaveRequestDTO;
import com.bilgeadam.dto.request.PaymentUpdateRequestDTO;
import com.bilgeadam.entity.Payment;
import com.bilgeadam.entity.enums.EStatus;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.FinanceServiceException;
import com.bilgeadam.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PaymentService {

    private PaymentRepository paymentRepository;

    public Boolean save(PaymentSaveRequestDTO dto) {
        paymentRepository.save(
                Payment.builder()
                        .accountId(dto.accountId())
                        .invoiceId(dto.invoiceId())
                        .paymentMethod(dto.paymentMethod())
                        .amount(dto.amount())
                        .paymentDate(dto.paymentDate())
                        .isPaid(dto.isPaid())
                        .build()
        );
        return true;
    }

    public Boolean delete(Long id) {
        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new FinanceServiceException(ErrorType.ACCOUNT_NOT_FOUND));
        payment.setStatus(EStatus.DELETED);
        paymentRepository.save(payment);
        return true;
    }

    public Boolean update(PaymentUpdateRequestDTO dto) {
        Payment payment = paymentRepository.findById(dto.id()).orElseThrow(() -> new FinanceServiceException(ErrorType.PAYMENT_NOT_FOUND));
        payment.setAccountId(dto.accountId());
        payment.setInvoiceId(dto.invoiceId());
        payment.setPaymentMethod(dto.paymentMethod());
        payment.setAmount(dto.amount());
        payment.setPaymentDate(dto.paymentDate());
        payment.setIsPaid(dto.isPaid());
        return true;
    }

    public Payment findById(Long id) {
        return paymentRepository.findById(id).orElseThrow(() -> new FinanceServiceException(ErrorType.PAYMENT_NOT_FOUND));
    }

    public List<Payment> findByAccountId(Long accountId) {
        return paymentRepository.findByAccountId(accountId);
    }

    public List<Payment> findByInvoiceId(Long invoiceId) {
        return paymentRepository.findByInvoiceId(invoiceId);
    }

    public List<Payment> findByPaymentStatus(Boolean isPaid) {
        return paymentRepository.findByIsPaid(isPaid);
    }


}
