package com.example.task_manager.controller;

import com.example.task_manager.dto.ApiResponse;
import com.example.task_manager.dto.TaskDTO;
import com.example.task_manager.model.Task;
import com.example.task_manager.service.TaskServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskServiceImpl taskService;

    public TaskController(TaskServiceImpl taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TaskDTO>> createTask(@RequestBody TaskDTO taskDTO) {
        Task task = taskService.createTask(taskDTO.toTask());
        ApiResponse<TaskDTO> response = new ApiResponse<>(HttpStatus.CREATED.value(), "Task created successfully",
                TaskDTO.fromTask(task));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskDTO>>> getAllTasks() {
        List<TaskDTO> tasks = taskService.getAllTasks().stream()
                .map(TaskDTO::fromTask)
                .collect(Collectors.toList());
        ApiResponse<List<TaskDTO>> response = new ApiResponse<>(HttpStatus.OK.value(), "Tasks retrieved successfully",
                tasks);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskDTO>> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(task -> {
                    ApiResponse<TaskDTO> response = new ApiResponse<>(HttpStatus.OK.value(),
                            "Task retrieved successfully", TaskDTO.fromTask(task));
                    return new ResponseEntity<>(response, HttpStatus.OK);
                })
                .orElseGet(() -> {
                    ApiResponse<TaskDTO> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Task not found",
                            null);
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                });
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskDTO>> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        Task updatedTask = taskService.updateTask(id, taskDTO.toTask());
        ApiResponse<TaskDTO> response = new ApiResponse<>(HttpStatus.OK.value(), "Task updated successfully",
                TaskDTO.fromTask(updatedTask));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        ApiResponse<Void> response = new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "Task deleted successfully",
                null);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
