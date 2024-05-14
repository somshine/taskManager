package com.somshine.taskmanager.model.response;

import lombok.Data;

@Data
public class TaskStatisticResponse extends BaseResponse {
    private Long totalTasks;
    private Long completedTasks;
    private Double percentCompleted;
}
