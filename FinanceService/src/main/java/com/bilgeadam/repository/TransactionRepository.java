package com.bilgeadam.repository;

import com.bilgeadam.entity.Transaction;
import com.bilgeadam.entity.enums.EExpenseCategory;
import com.bilgeadam.entity.enums.ETransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    List<Transaction> findByTypeAndCategoryOrderByCategoryAsc(ETransactionType type, EExpenseCategory category);

    List<Transaction> findByTypeAndCategoryOrderByCategoryDesc(ETransactionType type, EExpenseCategory category);

    List<Transaction> findByAccountId(Long accountId);

    List<Transaction> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate);

    List<Transaction> findByTransactionType(ETransactionType transactionType);
}

