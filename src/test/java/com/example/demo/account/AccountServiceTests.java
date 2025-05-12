package com.example.demo.account;

import com.example.demo.TestAppConfig;
import com.example.demo.exceptions.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Clock;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestAppConfig.class)
public class AccountServiceTests {
    @Autowired
    AccountService accountService;
    @Autowired
    Clock clock;

    private static final long UNKNOWN_ID = 123L;

    @Test
    void unknownIdThrowsNotFoundException() {
        // Could implement each assert as a separate @Test but soft assertions provide
        // a bit less code
        var softly = new SoftAssertions();

        softly.assertThatThrownBy(() -> accountService.getAccountById(UNKNOWN_ID))
                .isInstanceOf(EntityNotFoundException.class);
        softly.assertThatThrownBy(() -> accountService.deleteAccount(UNKNOWN_ID))
                .isInstanceOf(EntityNotFoundException.class);
        softly.assertThatThrownBy(() -> {
            var unknowAccount = createMockAccount();
            unknowAccount.setId(UNKNOWN_ID);
            accountService.updateAccount(unknowAccount);
        }).isInstanceOf(EntityNotFoundException.class);

        softly.assertAll();
    }

    @Test
    void createsAccount() {
        var newAccount = createMockAccount();
        Long id = accountService.createNewAccount(newAccount);
        assertThat(id).isNotNull();
    }

    @Test
    void creatingAccountWithMissingNameThrowsException() {
        var invalidNameAccount = createMockAccount();
        invalidNameAccount.setName(null);
        assertThatThrownBy(() -> accountService.createNewAccount(invalidNameAccount))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void creatingAccountWithInvalidPhoneNrThrowsException() {
        var invalidNameAccount = createMockAccount();
        invalidNameAccount.setPhoneNr("+37232947298374298347");
        assertThatThrownBy(() -> accountService.createNewAccount(invalidNameAccount))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void deletesAccount() {
        var existingAccount = createMockAccount();
        Long id = accountService.createNewAccount(existingAccount);
        assertThat(id).isNotNull();
        existingAccount.setId(id);

        accountService.deleteAccount(id);

        assertThatThrownBy(() -> accountService.deleteAccount(id))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void getsExistingAccount() {
        var existingAccount = createMockAccount();
        Long id = accountService.createNewAccount(existingAccount);
        assertThat(id).isNotNull();
        existingAccount.setId(id);

        var foundAccount = accountService.getAccountById(id);
        assertThat(foundAccount)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", existingAccount.getId())
                .hasFieldOrPropertyWithValue("name", existingAccount.getName())
                .hasFieldOrPropertyWithValue("phoneNr", existingAccount.getPhoneNr())
                .matches(acc -> acc.getCreatedAt().isEqual(existingAccount.getCreatedAt()))
                .matches(acc -> acc.getModifiedAt().isEqual(existingAccount.getModifiedAt()));
    }


    @Test
    void updatesAccount() {
        var existingAccount = createMockAccount();
        Long id = accountService.createNewAccount(existingAccount);
        assertThat(id).isNotNull();
        existingAccount.setId(id);

        var otherAccount = existingAccount.makeCopy();
        otherAccount.setId(id);
        otherAccount.setName("John");
        otherAccount.setPhoneNr("+37212345678");

        accountService.updateAccount(otherAccount);

        var updatedAccount = accountService.getAccountById(id);
        assertThat(updatedAccount)
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("name", "John")
                .hasFieldOrPropertyWithValue("phoneNr", "+37212345678")
                .hasFieldOrProperty("createdAt")
                .hasFieldOrProperty("modifiedAt");
    }

    private Account createMockAccount() {
        var existingAccount = new Account();
        existingAccount.setName("Rasmus");
        existingAccount.setPhoneNr("+37255512345");
        existingAccount.setCreatedAt(clock.instant().atZone(ZoneId.systemDefault()));
        existingAccount.setModifiedAt(clock.instant().atZone(ZoneId.systemDefault()));
        return existingAccount;
    }

}
