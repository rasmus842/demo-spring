package com.example.demo;

import com.example.demo.account.AccountService;
import com.example.demo.account.repo.AccountRepository;
import com.example.demo.account.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZoneId;

import static com.example.demo.TestAppConfig.FIXED_INSTANT;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class SpringDemoSanityTests extends BaseTest {
	@Autowired
	AccountService accountService;
	@Autowired
	AccountRepository accountRepo;
	@Autowired
	MockMvc mockMvc;

	@Test
	void contextLoads() {
		assertThat(accountRepo).isNotNull();
		assertThat(accountService).isNotNull();
	}
	@Test
	void testAccountRepo() {
		var account = new Account();
		account.setName("Rasmus");
		account.setCreatedAt(FIXED_INSTANT.atZone(ZoneId.systemDefault()));
		account.setModifiedAt(FIXED_INSTANT.atZone(ZoneId.systemDefault()).plusHours(1L));
		account.setPhoneNr("+37255512345");
		account = accountRepo.save(account);

		assertThat(account.getId()).isNotNull();
		assertThat(account.getId()).isEqualTo(1L);

		var existingAccount = accountRepo.findById(1L);
		assertThat(existingAccount).isNotEmpty();
		assertThat(existingAccount.get().getName()).isEqualTo("Rasmus");
	}

}
