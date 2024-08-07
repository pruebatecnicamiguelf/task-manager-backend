package com.example.task_manager.dto;

import com.example.task_manager.model.Status;
import com.example.task_manager.model.Task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class TaskDTO {
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;
    
    @NotNull(message = "Status cannot be null")
    @Pattern(regexp = "PENDING|COMPLETED", message = "Status must be either 'PENDING' or 'COMPLETED'")
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Task toTask() {
        Task task = new Task();
        task.setId(id);
        task.setTitle(title);
        task.setDescription(description);
        if (status != null) {
            task.setStatus(Status.valueOf(status)); // Convertir de String a Status enum
        }
        return task;
    }

    public static TaskDTO fromTask(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(task.getId());
        taskDTO.setTitle(task.getTitle());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setStatus(task.getStatus().name()); // Convertir de Status enum a String
        return taskDTO;
    }


}
