package com.example.demo.account;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.repo.dao.AccountRepository;
import com.example.demo.repo.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepo;
    private final Clock clock;

    @Override
    public Account getAccountById(long id) {
        var account = accountRepo.findById(id);
        if (account.isEmpty()) {
            throw new EntityNotFoundException(Account.class, id);
        }
        return account.get();
    }

    @Override
    public Long createNewAccount(Account newAccount) {
        // TODO validate name and phone number
        var now = clock.instant().atZone(ZoneId.systemDefault());
        newAccount.setCreatedAt(now);
        newAccount.setModifiedAt(now);
        var createdAccount = accountRepo.save(newAccount);
        return createdAccount.getId();
    }

    @Override
    public void updateAccount(Account account) {
        // TODO validate name and phone number
        long id = account.getId();
        var existingAccount = accountRepo.findById(account.getId());
        if (existingAccount.isEmpty()) {
            throw new EntityNotFoundException(Account.class, id);
        }
        var modifiedAt = clock.instant().atZone(ZoneId.systemDefault());
        int count =
                accountRepo.updateAccount(id, account.getName(), account.getPhoneNr(), modifiedAt);
        if (count > 1) {
            throw new IllegalStateException("Updated more than one account entities " + count);
        }
    }

    @Override
    public void deleteAccount(long id) {
        int count = accountRepo.deleteAccount(id);
        if (count == 0) {
            throw new EntityNotFoundException(Account.class, id);
        }
        if (count > 1) {
            throw new IllegalStateException("DELETED more than one account entities " + count);
        }
    }

}
