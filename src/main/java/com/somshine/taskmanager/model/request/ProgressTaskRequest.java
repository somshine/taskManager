package com.somshine.taskmanager.model.request;

import lombok.Data;

@Data
public class ProgressTaskRequest {
    private Long taskId;
    private Integer progress;
}
