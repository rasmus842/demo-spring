package com.example.demo.repo.dao;

import com.example.demo.repo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
        UPDATE account
        SET name = :name, phone_nr = :phoneNr, modified_at = :modifiedAt
        WHERE id = :id
        """)
    int updateAccount(long id, String name, String phoneNr, ZonedDateTime modifiedAt);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
        DELETE FROM account
        WHERE id = :id
        """)
    int deleteAccount(long id);
}
