package com.example.task_manager.service;

import com.example.task_manager.exception.ResourceNotFoundException;
import com.example.task_manager.model.Task;
import com.example.task_manager.model.User;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Task createTask(Task task) {
        User user = getCurrentUser();
        task.setUser(user);
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getAllTasks() {
        User user = getCurrentUser();
        return taskRepository.findByUser(user);
    }

    @Override
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id)
                .filter(task -> task.getUser().equals(getCurrentUser()))
                .map(Optional::of)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    @Override
    public Task updateTask(Long id, Task task) {
        Task existingTask = taskRepository.findById(id)
                .filter(t -> t.getUser().equals(getCurrentUser()))
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    
        // Imprimir para depuración
        System.out.println("Existing Task: " + existingTask);
    
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setStatus(task.getStatus());
        return taskRepository.save(existingTask);
    }

    @Override
    public void deleteTask(Long id) {
        Task existingTask = taskRepository.findById(id)
                .filter(t -> t.getUser().equals(getCurrentUser()))
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        taskRepository.delete(existingTask);
    }

    private User getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
