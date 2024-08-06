package com.example.task_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.task_manager.model.BlacklistedToken;

import java.util.Optional;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {
    Optional<BlacklistedToken> findByToken(String token);
}
