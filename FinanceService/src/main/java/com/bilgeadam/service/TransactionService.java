package com.bilgeadam.service;

import com.bilgeadam.dto.request.TransactionSaveRequestDTO;
import com.bilgeadam.dto.request.TransactionUpdateRequestDTO;
import com.bilgeadam.entity.Transaction;
import com.bilgeadam.entity.enums.EExpenseCategory;
import com.bilgeadam.entity.enums.EIncomeCategory;
import com.bilgeadam.entity.enums.EStatus;
import com.bilgeadam.entity.enums.ETransactionType;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.FinanceServiceException;
import com.bilgeadam.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final InvoiceService invoiceService;

    public Boolean save(TransactionSaveRequestDTO dto) {
        validateTransactionCategory(dto.transactionType(), dto.expenseCategory(), dto.incomeCategory());
        transactionRepository.save(
                Transaction.builder()
                        .accountId(dto.accountId())
                        .invoiceId(dto.invoiceId())
                        .transactionType(dto.transactionType())
                        .expenseCategory(dto.expenseCategory())
                        .incomeCategory(dto.incomeCategory())
                        .amount(dto.amount())
                        .description(dto.description())
                        .transactionDate(dto.transactionDate())
                        .build()
        );
        return true;
    }

    public Boolean delete(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new FinanceServiceException(ErrorType.TRANSACTION_NOT_FOUND));
        transaction.setStatus(EStatus.DELETED);
        transactionRepository.save(transaction);
        return true;
    }

    public Boolean update(TransactionUpdateRequestDTO dto) {
        Transaction transaction = transactionRepository.findById(dto.id()).orElseThrow(() -> new FinanceServiceException(ErrorType.TRANSACTION_NOT_FOUND));
        validateTransactionCategory(dto.transactionType(), dto.expenseCategory(), dto.incomeCategory());
        transaction.setInvoiceId(dto.invoiceId());
        transaction.setAccountId(dto.accountId());
        transaction.setTransactionType(dto.transactionType());
        transaction.setExpenseCategory(dto.expenseCategory());
        transaction.setIncomeCategory(dto.incomeCategory());
        transaction.setAmount(dto.amount());
        transaction.setDescription(dto.description());
        transaction.setTransactionDate(dto.transactionDate());

        return true;
    }

    // Gider kategorisine göre sıralama
    public List<Transaction> getSortedTransactionsByCategory(ETransactionType type, EExpenseCategory category, boolean ascending) {
        if (ascending) {
            return transactionRepository.findByTypeAndCategoryOrderByCategoryAsc(type, category);
        } else {
            return transactionRepository.findByTypeAndCategoryOrderByCategoryDesc(type, category);
        }
    }

    /**
     * Gelir ve gider işlemleri için kategori doğrulaması yapar.
     * @param transactionType İşlem tipi (Gelir veya Gider).
     * @param expenseCategory Eğer işlem giderse, bu parametre dolu olmalıdır.
     * @param incomeCategory Eğer işlem gelirse, bu parametre dolu olmalıdır.
     */
    private void validateTransactionCategory(ETransactionType transactionType,
                                             EExpenseCategory expenseCategory,
                                             EIncomeCategory incomeCategory) {
        if (transactionType == ETransactionType.INCOME && incomeCategory == null) {
            throw new IllegalArgumentException("Gelir işlemi için Sadece GELİR kategorisi belirtilmelidir.");
        }
        if (transactionType == ETransactionType.EXPENSE && expenseCategory == null) {
            throw new IllegalArgumentException("Gider işlemi için Sadece HARCAMA kategorisi belirtilmelidir.");
        }
    }
}
