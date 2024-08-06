package com.example.task_manager.repository;


import com.example.task_manager.model.Task;
import com.example.task_manager.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user);
}

