package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
@ComponentScan({
        "com.example.demo"
})
public class AppConfig {
    @Bean
    Clock clock() {
        return Clock.systemDefaultZone();
    }
}
