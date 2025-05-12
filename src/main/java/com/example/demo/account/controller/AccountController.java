package com.example.demo.account.controller;

import com.example.demo.account.AccountService;
import com.example.demo.generated.api.AccountsApi;
import com.example.demo.generated.model.AccountDTO;
import com.example.demo.generated.model.AccountRequest;
import com.example.demo.account.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AccountController implements AccountsApi {
    private final AccountService accountService;

    @Override
    @PostMapping("/accounts")
    public ResponseEntity<Long> createAccount(@RequestBody @Valid AccountRequest accountRequest) {
        var newAccount = mapRequestToAccount(accountRequest);
        Long generatedId = accountService.createNewAccount(newAccount);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(generatedId);
    }

    @Override
    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable("id") Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/account/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable("id") Long id) {
        var account = accountService.getAccountById(id);
        return ResponseEntity.ok(mapAccountToResponse(account));
    }

    @Override
    @PutMapping("/accounts/{id}")
    public ResponseEntity<Void> updateAccount(
            @PathVariable("id") Long id,
            @RequestBody @Valid AccountRequest accountRequest
    ) {
        var account = mapRequestToAccount(accountRequest);
        account.setId(id);
        accountService.updateAccount(account);
        return ResponseEntity.noContent().build();
    }

    private Account mapRequestToAccount(AccountRequest request) {
        var account = new Account();
        account.setName(request.getName());
        account.setPhoneNr(request.getPhoneNr());
        return account;
    }

    private AccountDTO mapAccountToResponse(Account account) {
        var dto = new AccountDTO();
        dto.setId(account.getId());
        dto.setName(account.getName());
        dto.setPhoneNr(account.getPhoneNr());
        dto.setCreatedAt(account.getCreatedAt().toOffsetDateTime());
        dto.setUpdatedAt(account.getModifiedAt().toOffsetDateTime());
        return dto;
    }
}
