

package com.bilgeadam.controller;

import com.bilgeadam.dto.request.AccountSaveRequestDTO;
import com.bilgeadam.dto.request.AccountUpdateRequestDTO;
import com.bilgeadam.dto.request.PageRequestDTO;
import com.bilgeadam.dto.response.AccountResponseDTO;
import com.bilgeadam.entity.Account;
import com.bilgeadam.entity.enums.ECurrency;
import com.bilgeadam.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static com.bilgeadam.constant.RestApis.*;

    @RestController
    @RequestMapping(ACCOUNT)
    @RequiredArgsConstructor
    @CrossOrigin("*")
    public class AccountController {
        private final AccountService accountService;

        @PostMapping(SAVE_ACCOUNT)
        @Operation(summary = "Creates New Account")
        public ResponseEntity<Boolean> save(@RequestBody AccountSaveRequestDTO dto) {
            return ResponseEntity.ok(accountService.saveAccount(dto));
        }

        @DeleteMapping(DELETE_ACCOUNT + "/{id}")
        @Operation(summary = "Deletes an account by ID")
        public ResponseEntity<Boolean> delete(@PathVariable Long id) {
            return ResponseEntity.ok(accountService.deleteAccount(id));
        }
        @PutMapping(UPDATE_ACCOUNT)
        @Operation(summary = "Updates an account")
        public ResponseEntity<Boolean> update(@RequestBody AccountUpdateRequestDTO dto) {
            return ResponseEntity.ok(accountService.updateAccount(dto));
        }

        @GetMapping(GET_ALL_ACCOUNTS)
        public ResponseEntity<List<Account>> findAll(@RequestBody PageRequestDTO dto){

            return ResponseEntity.ok(accountService.findAll(dto));
        }

        @GetMapping(GET_ACCOUNT_BY_ID + "/{id}")
        @Operation(summary = "Get account by ID")
        public ResponseEntity<Account> findById(@PathVariable Long id) {
            return ResponseEntity.ok(accountService.findById(id));
        }

        @GetMapping(GET_ACCOUNT_BY_ACCOUNT_NUMBER + "/{accountNumber}")
        @Operation(summary = "Get account by account number")
        public ResponseEntity<AccountResponseDTO> getByAccountNumber(@PathVariable String accountNumber) {
            Account account = accountService.findByAccountNumber(accountNumber);
            return ResponseEntity.ok(new AccountResponseDTO(account));
        }

        @GetMapping(GET_ACCOUNT_BY_COMPANY_NAME + "/{companyName}")
        @Operation(summary = "Get account by company name")
        public ResponseEntity<AccountResponseDTO> getByCompanyName(@PathVariable String companyName) {
            Account account = accountService.findByCompanyName(companyName);
            return ResponseEntity.ok(new AccountResponseDTO(account));
        }

        @GetMapping("/by-currency-balance")
        @Operation(summary = "Get accounts by currency and balance")
        public ResponseEntity<List<AccountResponseDTO>> getByCurrencyAndBalance(
                @RequestParam ECurrency currency,
                @RequestParam BigDecimal balance) {
            List<Account> accounts = accountService.findByCurrencyAndBalanceGreaterThan(currency, balance);
            List<AccountResponseDTO> response = accounts.stream()
                    .map(AccountResponseDTO::new)
                    .toList();
            return ResponseEntity.ok(response);
        }
    }


