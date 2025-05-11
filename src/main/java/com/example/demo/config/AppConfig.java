package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.Clock;

@Configuration
@ComponentScan({
        "com.example.demo"
})
@EnableTransactionManagement
public class AppConfig {
    @Bean
    Clock clock() {
        return Clock.systemDefaultZone();
    }
}
