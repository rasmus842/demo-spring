package com.example.demo.account;

import com.example.demo.BaseTest;
import com.example.demo.generated.model.AccountDTO;
import com.example.demo.generated.model.AccountRequest;
import com.example.demo.generated.model.ErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountIntegrationTests extends BaseTest {
    @LocalServerPort
    int port;

    RestTemplate restTemplate;
    String baseUrl;

    private final Long unknownId = 123443215L;
    private final String defaultName = "Rasmus";
    private final String defaultPhoneNr = "+37253477411";

    @BeforeEach
    void setup() {
        restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(response -> false);
        baseUrl = "http://localhost:" + port;
    }

    @Test
    void testAccountCreation() {
        postAccount();
    }

    @Test
    void doesNotGetUnknownAccount() {
        ResponseEntity<ErrorMessage> resp =
                restTemplate.getForEntity(baseUrl + "/account/" + unknownId, ErrorMessage.class);
        assertThat(resp.getStatusCode().value()).isEqualTo(404);
    }

    @Nested
    class AfterAccountCreatedTests {
        Long accountId;

        @BeforeEach
        void createAccount() {
            accountId = postAccount();
        }

        @Test
        void getsAccount() {
            var account = getAccount(accountId);
            assertAccountProps(account, defaultName, defaultPhoneNr);
        }

        @Test
        void updatesAccountByChangingNameAndPhoneNr() {
            String newName = "Markus";
            String newPhoneNr = "+372555123456";
            var req = new AccountRequest();
            req.setName(newName);
            req.setPhoneNr(newPhoneNr);
            restTemplate.put(baseUrl + "/accounts/" + accountId, req);

            var account = getAccount(accountId);
            assertAccountProps(account, newName, newPhoneNr);
        }

        @Test
        void deletesAccount() {
            restTemplate.delete(baseUrl + "/accounts/" + accountId);

            ResponseEntity<ErrorMessage> resp =
                    restTemplate.getForEntity(baseUrl + "/account/" + accountId, ErrorMessage.class);
            assertThat(resp.getStatusCode().value()).isEqualTo(404);
        }

    }

    private Long postAccount() {
        var req = new AccountRequest();
        req.setName(defaultName);
        req.setPhoneNr(defaultPhoneNr);

        ResponseEntity<Long> resp = restTemplate.postForEntity(
                baseUrl  + "/accounts",
                req,
                Long.class
        );
        assertThat(resp.getStatusCode().value()).isEqualTo(201);
        Long id = resp.getBody();
        assertThat(id).isNotNull();
        return id;
    }

    private AccountDTO getAccount(Long id) {
        ResponseEntity<AccountDTO> resp =
                restTemplate.getForEntity(baseUrl + "/account/" + id, AccountDTO.class);
        assertThat(resp.getStatusCode().value()).isEqualTo(200);
        return resp.getBody();
    }

    private void assertAccountProps(AccountDTO account, String name, String phoneNr) {
        assertThat(account).isNotNull()
                .matches(a -> a.getName().equals(name))
                .matches(a -> a.getPhoneNr().equals(phoneNr))
                .matches(a -> a.getCreatedAt() != null)
                .matches(a -> a.getUpdatedAt() != null);
    }
}
