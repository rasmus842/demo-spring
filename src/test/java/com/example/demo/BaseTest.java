package com.example.demo;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestAppConfig.class)
public abstract class BaseTest {

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        String profile = System.getProperty("spring.profiles.active", "");
        if ("mySqlTest".equalsIgnoreCase(profile)) {
            var container = new MySQLContainer<>("mysql:8.0")
                    .withDatabaseName("demo-spring")
                    .withUsername("demo-spring")
                    .withPassword("demo-spring");
            container.start();
            registry.add("spring.datasource.url", container::getJdbcUrl);
            registry.add("spring.datasource.username", container::getUsername);
            registry.add("spring.datasource.password", container::getPassword);
            registry.add("spring.datasource.driver-class-name", container::getDriverClassName);
            Runtime.getRuntime()
                    .addShutdownHook(new Thread(container::stop));
        }
    }

}
