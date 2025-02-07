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
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final InvoiceService invoiceService;

    /*Buradaki save metodu kaydın önce gelir mi gider olduğuna bakılarak çalışır.
      Gelir ya da gider olarak seçildikten sonra kayıt işlemi yapar. -validate(doğrulama metodu)
   */
    @Operation(summary = "TRANSACTION SAVE")
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

    @Operation(summary = "TRANSACTION DELETE")
    public Boolean delete(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new FinanceServiceException(ErrorType.TRANSACTION_NOT_FOUND));
        transaction.setStatus(EStatus.DELETED);
        transactionRepository.save(transaction);
        return true;
    }

    @Operation(summary = "TRANSACTION UPDATE")
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
    @Operation(summary = "Gider Kategorisine Göre Sıralama")
    public List<Transaction> getSortedTransactionsByCategory(ETransactionType type, EExpenseCategory category, boolean ascending) {
        if (ascending) {
            return transactionRepository.findByTypeAndCategoryOrderByCategoryAsc(type, category);
        } else {
            return transactionRepository.findByTypeAndCategoryOrderByCategoryDesc(type, category);
        }
    }

    /**
     * Gelir ve gider işlemleri için kategori doğrulaması yapar.
     *
     * @param transactionType İşlem tipi (Gelir veya Gider).
     * @param expenseCategory Eğer işlem giderse, bu parametre dolu olmalıdır.
     * @param incomeCategory  Eğer işlem gelirse, bu parametre dolu olmalıdır.
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

    @Operation(summary = "Hesap Numarasına Göre Gelir-Gider Bulma")
    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    @Operation(summary = "İki Tarih Aralığındaki Gelir-Giderler")
    public List<Transaction> getTransactionsByDateRange(LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByTransactionDateBetween(startDate, endDate);
    }

    @Operation(summary = "İşlem Türüne(Gelir-Gider) Göre Kayıt Bulma")
    public List<Transaction> getTransactionsByType(ETransactionType transactionType) {
        return transactionRepository.findByTransactionType(transactionType);
    }

    @Operation(summary = "Toplam Gelir")
    public BigDecimal getTotalIncome() {
        List<Transaction> incomeTransactions = transactionRepository.findByTransactionType(ETransactionType.INCOME);
        return incomeTransactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Operation(summary = "Toplam Gider")
    public BigDecimal getTotalExpense() {
        List<Transaction> expenseTransactions = transactionRepository.findByTransactionType(ETransactionType.EXPENSE);
        return expenseTransactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Operation(summary = "Net Kar")
    public BigDecimal getNetProfit() {

        return getTotalIncome().subtract(getTotalExpense());
    }
}
