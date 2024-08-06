package com.example.task_manager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.task_manager.service.DatabaseTokenBlacklistService;
import com.example.task_manager.service.InMemoryTokenBlacklistService;
import com.example.task_manager.service.TokenBlacklistService;

@Configuration
public class BlacklistConfig {

    @Value("${token.blacklist.implementation:memory}")
    private String blacklistImplementation;

    private final InMemoryTokenBlacklistService inMemoryService;
    private final DatabaseTokenBlacklistService databaseService;

    public BlacklistConfig(InMemoryTokenBlacklistService inMemoryService, DatabaseTokenBlacklistService databaseService) {
        this.inMemoryService = inMemoryService;
        this.databaseService = databaseService;
    }

    @Bean
    public TokenBlacklistService tokenBlacklistService() {
        switch (blacklistImplementation.toLowerCase()) {
            case "database":
                return databaseService;
            case "memory":
            default:
                return inMemoryService;
        }
    }
}
