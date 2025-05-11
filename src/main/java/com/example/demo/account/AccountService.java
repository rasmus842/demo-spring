package com.example.demo.account;

import com.example.demo.repo.entity.Account;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;


public interface AccountService {
    Account getAccountById(long id);

    Long createNewAccount(@NotNull @Valid Account account);

    void updateAccount(@NotNull @Valid Account account);

    void deleteAccount(long id);
}
