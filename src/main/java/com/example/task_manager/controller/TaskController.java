package com.example.task_manager.controller;

import com.example.task_manager.dto.ApiResponse;
import com.example.task_manager.dto.TaskDTO;
import com.example.task_manager.exception.ResourceNotFoundException;
import com.example.task_manager.model.Task;
import com.example.task_manager.service.TaskServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
// import io.swagger.v3.oas.annotations.responses.ApiResponse; // Cambiar el nombre de la clase interna si es necesario
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
@Validated
public class TaskController {
    private final TaskServiceImpl taskService;

    public TaskController(TaskServiceImpl taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Create a new task")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Task created successfully", content = @Content(schema = @Schema(implementation = TaskDTO.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ApiResponse<TaskDTO>> createTask(@RequestBody @Valid TaskDTO taskDTO) {
        Task task = taskService.createTask(taskDTO.toTask());
        ApiResponse<TaskDTO> response = new ApiResponse<>(HttpStatus.CREATED.value(), "Task created successfully",
                TaskDTO.fromTask(task));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all tasks")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Tasks retrieved successfully", content = @Content(schema = @Schema(implementation = TaskDTO.class)))
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskDTO>>> getAllTasks() {
        List<TaskDTO> tasks = taskService.getAllTasks().stream()
                .map(TaskDTO::fromTask)
                .collect(Collectors.toList());
        ApiResponse<List<TaskDTO>> response = new ApiResponse<>(HttpStatus.OK.value(), "Tasks retrieved successfully",
                tasks);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get task by ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Task retrieved successfully", content = @Content(schema = @Schema(implementation = TaskDTO.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Task not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskDTO>> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        ApiResponse<TaskDTO> response = new ApiResponse<>(HttpStatus.OK.value(), "Task retrieved successfully",
                TaskDTO.fromTask(task));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Update task")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Task updated successfully", content = @Content(schema = @Schema(implementation = TaskDTO.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Task not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskDTO>> updateTask(@PathVariable Long id, @RequestBody @Valid TaskDTO taskDTO) {
        Task updatedTask = taskService.updateTask(id, taskDTO.toTask());
        ApiResponse<TaskDTO> response = new ApiResponse<>(HttpStatus.OK.value(), "Task updated successfully",
                TaskDTO.fromTask(updatedTask));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Delete task")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Task deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Task not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        ApiResponse<Void> response = new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "Task deleted successfully",
                null);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
