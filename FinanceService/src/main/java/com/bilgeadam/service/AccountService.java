package com.bilgeadam.service;

import com.bilgeadam.dto.request.AccountSaveRequestDTO;
import com.bilgeadam.dto.request.AccountUpdateRequestDTO;
import com.bilgeadam.dto.request.PageRequestDTO;
import com.bilgeadam.entity.Account;
import com.bilgeadam.entity.enums.ECurrency;
import com.bilgeadam.entity.enums.EStatus;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.FinanceServiceException;
import com.bilgeadam.repository.AccountRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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
                        .companyName(dto.companyName())
                        .bankName(dto.bankName())
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
        account.setCompanyName(dto.companyName());
        account.setBankName(dto.bankName());
        account.setBankName(dto.bankName());
        account.setAccountNumber(dto.accountName());
        account.setCurrency(dto.currency());
        account.setBalance(dto.balance());

        return true;
    }

    // ID ile Bulma
    public Account findById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new FinanceServiceException(ErrorType.ACCOUNT_NOT_FOUND));
    }

    //Bütün banka Hesaplarını Bulma --> Banka ismine göre
    public List<Account> findAll(PageRequestDTO dto) {
        String bankName = dto.searchText();
        if (bankName != null && !bankName.isEmpty()) {
            return accountRepository.findByBankNameContainingIgnoreCaseAndStatusNot(bankName, EStatus.DELETED, PageRequest.of(dto.page(), dto.size())).getContent();
        }
        return accountRepository.findAllByStatusNot(EStatus.DELETED, PageRequest.of(dto.page(), dto.size())).getContent();
    }

    /**
     * Hesap numarasına göre hesabı bulur.
     *
     * @param accountNumber Aranan hesap numarası.
     * @return Bulunan hesap veya hata fırlatır.
     */
    public Account findByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new FinanceServiceException(ErrorType.ACCOUNT_NOT_FOUND));
    }

    /**
     * Şirket adına göre hesabı bulur.
     *
     * @param companyName Aranan şirket adı.
     * @return Bulunan hesap veya hata fırlatır.
     */
    public Account findByCompanyName(String companyName) {
        return accountRepository.findByCompanyName(companyName)
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

