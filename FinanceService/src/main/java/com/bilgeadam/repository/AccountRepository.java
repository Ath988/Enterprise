package com.bilgeadam.repository;

import com.bilgeadam.dto.response.AccountResponseDTO;
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
    @Query("SELECT i FROM Account i WHERE LOWER(i.accountNumber) LIKE LOWER(CONCAT('%', :accountNumber, '%'))")
    Optional<Account> findByAccountNumberContainingIgnoreCase(@Param("accountNumber") String accountNumber);


    // Hesap adına göre hesap bulma
    @Query("SELECT i FROM Account i WHERE LOWER(i.accountName) LIKE LOWER(CONCAT('%', :accountName, '%'))")
    Optional<Account> findByAccountNameContainingIgnoreCase(@Param("accountName") String accountName);

    // Para birimi ve bakiye bilgisi ile sorgulama
    @Query("SELECT i FROM Account i WHERE i.currency = :currency AND i.balance > :balance")
    List<Account> findByCurrencyAndBalanceGreaterThan(@Param("currency") ECurrency currency, @Param("balance") BigDecimal balance);



    Page<Account> findAllByStatusNot (EStatus status, Pageable pageable);

}
