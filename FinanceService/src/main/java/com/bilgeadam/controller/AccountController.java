

package com.bilgeadam.controller;

import com.bilgeadam.dto.request.AccountSaveRequestDTO;
import com.bilgeadam.dto.request.AccountUpdateRequestDTO;
import com.bilgeadam.dto.request.PageRequestDTO;
import com.bilgeadam.dto.response.AccountResponseDTO;
import com.bilgeadam.dto.response.ResponseDTO;
import com.bilgeadam.entity.Account;
import com.bilgeadam.entity.enums.ECurrency;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.FinanceServiceException;
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
        public ResponseEntity<ResponseDTO<Boolean>> save(@RequestBody AccountSaveRequestDTO dto) {
            return ResponseEntity.ok(ResponseDTO
                    .<Boolean>builder()
                    .data(accountService.saveAccount(dto))
                    .message("Success")
                    .code(200)
                    .build());
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

        @PostMapping(GET_ALL_ACCOUNTS)
        public ResponseEntity<ResponseDTO<List<Account>>> findAll(@RequestBody PageRequestDTO dto) {
            return ResponseEntity.ok(ResponseDTO
                    .<List<Account>>builder()
                    .data(accountService.findAll(dto))
                    .message("Success")
                    .code(200)
                    .build());
        }


        @PostMapping(GET_ACCOUNT_BY_ID + "/{id}")
        @Operation(summary = "Get account by ID")
        public ResponseEntity<Account> findById(@PathVariable Long id) {
            return ResponseEntity.ok(accountService.findById(id));
        }



        @GetMapping(GET_ACCOUNT_BY_ACCOUNT_NUMBER + "/{accountNumber}")
        @Operation(summary = "Get account by account number")
        public ResponseEntity<AccountResponseDTO> findByAccountNumberContainingIgnoreCase(@PathVariable String accountNumber) {
            Account account = accountService.findByAccountNumberContainingIgnoreCase(accountNumber);

            if (account == null) {
                throw new FinanceServiceException(ErrorType.ACCOUNT_NOT_FOUND); // Eğer account bulunamazsa, hata fırlatıyoruz
            }

            // Account nesnesinden gelen bilgileri AccountResponseDTO'ya çeviriyoruz
            AccountResponseDTO accountResponseDTO = new AccountResponseDTO(
                    account.getId(),
                    account.getAccountName(),
                    account.getAccountNumber(),
                    account.getCurrency(),
                    account.getBalance()
            );

            return ResponseEntity.ok(accountResponseDTO);
        }

        @GetMapping(GET_ACCOUNT_BY_ACCOUNT_NAME + "/{accountName}")
        @Operation(summary = "Get account by account name")
        public ResponseEntity<AccountResponseDTO> findByAccountNameContainingIgnoreCase(@PathVariable String accountName) {
            Account account = accountService.findByAccountNameContainingIgnoreCase(accountName);

            if (account == null) {
                throw new FinanceServiceException(ErrorType.ACCOUNT_NOT_FOUND); // Eğer account bulunamazsa, hata fırlatıyoruz
            }

            // Account nesnesinden gelen bilgileri AccountResponseDTO'ya çeviriyoruz
            AccountResponseDTO accountResponseDTO = new AccountResponseDTO(
                    account.getId(),
                    account.getAccountName(),
                    account.getAccountNumber(),
                    account.getCurrency(),
                    account.getBalance()
            );

            return ResponseEntity.ok(accountResponseDTO);
        }

    }


