package com.example.demo.account;

import com.example.demo.repo.entity.Account;


public interface AccountService {
    Account getAccountById(long id);

    Long createNewAccount(Account account);

    void updateAccount(Account account);

    void deleteAccount(long id);
}
