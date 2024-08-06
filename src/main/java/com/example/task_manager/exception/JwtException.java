package com.example.task_manager.exception;

public class JwtException extends RuntimeException {
    public JwtException(String message) {
        super(message);
    }
}