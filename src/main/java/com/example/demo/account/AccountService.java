package com.example.demo.account;

import com.example.demo.repo.entity.Account;

import java.util.Optional;

public interface AccountService {
    Optional<Account> getAccountById(long id);
}
