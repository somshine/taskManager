package com.somshine.taskmanager.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserTaskDto {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String taskPriority;
    private String assignedUser;
    private LocalDateTime dueDate;
    private Integer progress;
    private LocalDateTime completedOn;
    private LocalDateTime createdOn;

}
