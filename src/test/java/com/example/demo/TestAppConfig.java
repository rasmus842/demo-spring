package com.example.demo;

import com.example.demo.config.AppConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@TestConfiguration
@Import(AppConfig.class)
public class TestAppConfig {
    public static final Instant FIXED_INSTANT = Instant.parse("2025-05-11T10:00:00.00Z");

    @Bean
    @Primary
    Clock fixedClock() {
        return Clock.fixed(FIXED_INSTANT, ZoneId.of("UTC"));
    }
}
