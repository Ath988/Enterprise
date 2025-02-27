package com.bilgeadam.service;

import com.bilgeadam.dto.request.AccountSaveRequestDTO;
import com.bilgeadam.dto.request.AccountUpdateRequestDTO;
import com.bilgeadam.dto.request.PageRequestDTO;
import com.bilgeadam.dto.response.AccountResponseDTO;
import com.bilgeadam.entity.Account;
import com.bilgeadam.entity.enums.ECurrency;
import com.bilgeadam.entity.enums.EStatus;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.FinanceServiceException;
import com.bilgeadam.repository.AccountRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    // DTO ile SAVE ISLEMI
    public Boolean saveAccount(AccountSaveRequestDTO dto) {
        accountRepository.save(
                Account.builder()
                        .accountName(dto.accountName())
                        .accountNumber(dto.accountNumber())
                        .currency(dto.currency())
                        .balance(dto.balance())
                        .build()
        );
        return true;
    }

    // DTO ile DELETE ISLEMI
    public Boolean deleteAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new FinanceServiceException(ErrorType.ACCOUNT_NOT_FOUND));
        account.setStatus(EStatus.DELETED);
        accountRepository.save(account);
        return true;
    }

    // DTO ile UPDATE ISLEMI
    public Boolean updateAccount(AccountUpdateRequestDTO dto) {

        Account account = accountRepository.findById(dto.id()).orElseThrow(() -> new FinanceServiceException(ErrorType.ACCOUNT_NOT_FOUND));
        account.setAccountName(dto.accountName());
        account.setAccountNumber(dto.accountNumber());
        account.setCurrency(dto.currency());
        account.setBalance(dto.balance());
        accountRepository.save(account);
        return true;
    }

    // ID ile Bulma
    public Account findById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new FinanceServiceException(ErrorType.ACCOUNT_NOT_FOUND));
    }

    public List<Account> findAll(PageRequestDTO dto) {
        return accountRepository.findAll();
    }


    /**
     * Hesap numarasına göre hesabı bulur.
     *
     * @param accountNumber Aranan hesap numarası.
     * @return Bulunan hesap veya hata fırlatır.
     */
    public Account findByAccountNumberContainingIgnoreCase(String accountNumber) {
        return accountRepository.findByAccountNumberContainingIgnoreCase(accountNumber)
                .orElseThrow(() -> new FinanceServiceException(ErrorType.ACCOUNT_NOT_FOUND));
    }
    public Account findByAccountNameContainingIgnoreCase(String accountName) {
        return accountRepository.findByAccountNameContainingIgnoreCase(accountName)
                .orElseThrow(() -> new FinanceServiceException(ErrorType.ACCOUNT_NOT_FOUND));
    }


    /**
     * Belirli bir para biriminde ve belirli bir bakiyeden yüksek olan hesapları getirir.
     *
     * @param currency Para birimi (USD, EUR, TRY vb.).
     * @param balance Minimum bakiye.
     * @return Şartları sağlayan hesap listesi.
     */
    public List<Account> findByCurrencyAndBalanceGreaterThan(ECurrency currency, BigDecimal balance) {
        return accountRepository.findByCurrencyAndBalanceGreaterThan(currency, balance);
    }

}

