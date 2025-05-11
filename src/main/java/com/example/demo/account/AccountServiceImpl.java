package com.example.demo.account;

import com.example.demo.repo.entity.Account;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Override
    public Optional<Account> getAccountById(long id) {
        return Optional.empty();
    }

}
