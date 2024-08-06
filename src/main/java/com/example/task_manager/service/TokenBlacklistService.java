package com.example.task_manager.service;

public interface TokenBlacklistService {
    void addToBlacklist(String token, long expirationTimeInMillis);
    boolean isTokenBlacklisted(String token);
}
