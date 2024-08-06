package com.example.task_manager.service;

import org.springframework.stereotype.Service;

import com.example.task_manager.model.BlacklistedToken;
import com.example.task_manager.repository.BlacklistedTokenRepository;

import java.util.Date;
import java.util.Optional;

@Service
public class DatabaseTokenBlacklistService implements TokenBlacklistService {

    private final BlacklistedTokenRepository repository;

    public DatabaseTokenBlacklistService(BlacklistedTokenRepository repository) {
        this.repository = repository;
    }

    @Override
    public void addToBlacklist(String token, long expirationTimeInMillis) {
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTimeInMillis);
        BlacklistedToken blacklistedToken = new BlacklistedToken();
        blacklistedToken.setToken(token);
        blacklistedToken.setExpirationDate(expirationDate);
        repository.save(blacklistedToken);
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        Optional<BlacklistedToken> optional = repository.findByToken(token);
        return optional.map(blacklistedToken -> blacklistedToken.getExpirationDate().after(new Date())).orElse(false);
    }
}
