package com.example.demo;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestAppConfig.class)
public abstract class BaseTest {

    /*
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        String profile = System.getProperty("spring.profiles.active", "");
        if (!"mySqlTest".equalsIgnoreCase(profile)) {
            return;
        }
        var container = new MySQLContainer<>("mysql:8.0")
                .withDatabaseName("demo-spring")
                .withUsername("demo-spring")
                .withPassword("demo-spring");
        container.start();
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }
     */
}
