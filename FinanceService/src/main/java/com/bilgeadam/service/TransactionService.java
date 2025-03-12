package com.bilgeadam.service;

import com.bilgeadam.dto.request.AccountUpdateRequestDTO;
import com.bilgeadam.dto.request.BudgetUpdateRequestDTO;
import com.bilgeadam.dto.request.TransactionSaveRequestDTO;
import com.bilgeadam.dto.request.TransactionUpdateRequestDTO;
import com.bilgeadam.dto.response.DepartmentResponseDTO;
import com.bilgeadam.entity.Account;
import com.bilgeadam.entity.Budget;
import com.bilgeadam.entity.Transaction;
import com.bilgeadam.entity.enums.EExpenseCategory;
import com.bilgeadam.entity.enums.EIncomeCategory;
import com.bilgeadam.entity.enums.EStatus;
import com.bilgeadam.entity.enums.ETransactionType;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.FinanceServiceException;
import com.bilgeadam.manager.IDepartmentClient;
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
    private final BudgetService budgetService;
    private final IDepartmentClient departmentClient;




    /*Buradaki save metodu kaydın önce gelir mi gider olduğuna bakılarak çalışır.
      Gelir ya da gider olarak seçildikten sonra kayıt işlemi yapar. -validate(doğrulama metodu)
   */

    @Operation(summary = "TRANSACTION SAVE")
    public Boolean save(TransactionSaveRequestDTO dto) {

        Account account = accountService.findById(dto.accountId());
        Budget budget = budgetService.findById(dto.budgetId());

        // Eğer Gider ise Department ve Bütçe kontrolü yapılıyor
        if (dto.transactionType() == ETransactionType.EXPENSE) {
            DepartmentResponseDTO department = departmentClient.getDepartmentById(dto.departmentId());

            if (department == null) {
                throw new FinanceServiceException(ErrorType.DEPARTMENT_NOT_FOUND);
            }

            BigDecimal remainingBudget = budget.getAllocatedAmount().subtract(budget.getSpentAmount());
            if (dto.amount().compareTo(remainingBudget) > 0) {
                throw new FinanceServiceException(ErrorType.INSUFFICIENT_BUDGET);
            }

            budget.setSpentAmount(budget.getSpentAmount().add(dto.amount()));
            budgetService.update(new BudgetUpdateRequestDTO(
                    budget.getId(),
                    budget.getYear(),
                    budget.getMonth(),
                    budget.getDepartmentId(),
                    budget.getBudgetCategory(),
                    budget.getDescription(),
                    budget.getAllocatedAmount(),
                    budget.getSpentAmount()

            ));
        }

        // Eğer Gelir ise sadece hesap bakiyesi güncellenir
        if (dto.transactionType() == ETransactionType.INCOME) {
            account.setBalance(account.getBalance().add(dto.amount()));

            // Account'u AccountUpdateRequestDTO'ya dönüştürüyoruz
            AccountUpdateRequestDTO accountUpdateRequestDTO = new AccountUpdateRequestDTO(
                    account.getId(),
                    account.getBalance(),
                    account.getAccountName(),
                    account.getAccountNumber(),
                    account.getCurrency());

            accountService.updateAccount(accountUpdateRequestDTO);
        }

        // Transaction'ı oluşturuyoruz
        Transaction transaction = Transaction.builder()
                .accountId(account.getId())
                .budgetId(budget.getId())
                .transactionType(dto.transactionType())
                .amount(dto.amount())
                .description(dto.description())
                .date(dto.transactionDate())
                .build();

        transactionRepository.save(transaction);
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
        // Mevcut Transaction'ı bul
        Transaction transaction = transactionRepository.findById(dto.id())
                .orElseThrow(() -> new FinanceServiceException(ErrorType.TRANSACTION_NOT_FOUND));

        // Account ve Budget'ı service katmanları üzerinden bul
        Account account = accountService.findById(dto.accountId());
        Budget budget = budgetService.findById(dto.budgetId());

        // Transaction'ı güncelle
        transaction.setAccountId(account.getId());  // accountId ile set ediliyor
        transaction.setBudgetId(budget.getId());     // budgetId ile set ediliyor
        transaction.setTransactionType(dto.transactionType());
        transaction.setAmount(dto.amount());
        transaction.setDescription(dto.description());
        transaction.setDate(dto.transactionDate());

        transactionRepository.save(transaction);
        return true;
    }

    @Operation(summary = "Hesap Numarasına Göre Gelir-Gider Bulma")
    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
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

//    /**
//     * Gelir ve gider işlemleri için kategori doğrulaması yapar.
//     *
//     * @param transactionType İşlem tipi (Gelir veya Gider).
//     * @param expenseCategory Eğer işlem giderse, bu parametre dolu olmalıdır.
//     * @param incomeCategory  Eğer işlem gelirse, bu parametre dolu olmalıdır.
//     */
//    private void validateTransactionCategory(ETransactionType transactionType,
//                                             EExpenseCategory expenseCategory,
//                                             EIncomeCategory incomeCategory) {
//        if (transactionType == ETransactionType.INCOME && incomeCategory == null) {
//            throw new IllegalArgumentException("Gelir işlemi için Sadece GELİR kategorisi belirtilmelidir.");
//        }
//        if (transactionType == ETransactionType.EXPENSE && expenseCategory == null) {
//            throw new IllegalArgumentException("Gider işlemi için Sadece HARCAMA kategorisi belirtilmelidir.");
//        }
//    }
}
