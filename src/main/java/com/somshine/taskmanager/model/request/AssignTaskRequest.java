package com.somshine.taskmanager.model.request;

import lombok.Data;

@Data
public class AssignTaskRequest {
    private Long userId;
    private Long taskId;
}
