package com.bilgeadam.repository;

import com.bilgeadam.entity.Account;
import com.bilgeadam.entity.enums.ECurrency;
import com.bilgeadam.entity.enums.EStatus;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountRepository  extends JpaRepository<Account, Long> {
    // Hesap numarasına göre hesap bulma
    @Query("SELECT i FROM Account i WHERE i.accountNumber = :accountNumber")
    Optional<Account> findByAccountNumber(@Param("accountNumber") String accountNumber);

    // Şirket adına göre hesap arama
    @Query("SELECT i FROM Account i WHERE i.companyName = :companyName")
    Optional<Account> findByCompanyName(@Param("companyName") String companyName);

    // Para birimi ve bakiye bilgisi ile sorgulama
    @Query("SELECT i FROM Account i WHERE i.currency = :currency AND i.balance > :balance")
    List<Account> findByCurrencyAndBalanceGreaterThan(@Param("currency") ECurrency currency, @Param("balance") BigDecimal balance);

    // Banka Adı ile Sorgulama
    @Query("SELECT i FROM Account i WHERE i.bankName LIKE %:bankName% AND i.status != :status")
    Page<Account> findByBankNameContainingIgnoreCaseAndStatusNot(@Param("bankName") String bankName, @Param("status") EStatus status, Pageable pageable);

    Page<Account> findAllByStatusNot(EStatus status, Pageable pageable);
}
