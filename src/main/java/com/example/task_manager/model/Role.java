package com.example.task_manager.model;

public enum Role {
    USER,
    ADMIN;

    public String getRoleName() {
        return name();
    }
}