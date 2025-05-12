package com.example.demo.account;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank
    @Size(min = 1, max = 255)
    private String name;

    @Column(name = "phone_nr", nullable = true)
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$")
    private String phoneNr;

    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "modified_at", nullable = false)
    private ZonedDateTime modifiedAt;

    public Account makeCopy() {
        var newAccount = new Account();
        newAccount.setName(this.getName());
        newAccount.setPhoneNr(this.getPhoneNr());
        newAccount.setCreatedAt(this.getCreatedAt());
        newAccount.setModifiedAt(this.getModifiedAt());
        return newAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id)
                && Objects.equals(name, account.name)
                && Objects.equals(phoneNr, account.phoneNr)
                && Objects.equals(createdAt, account.createdAt)
                && Objects.equals(modifiedAt, account.modifiedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, phoneNr, createdAt, modifiedAt);
    }
}