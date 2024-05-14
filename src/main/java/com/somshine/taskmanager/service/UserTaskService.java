package com.somshine.taskmanager.service;

import com.somshine.taskmanager.config.TaskStatuses;
import com.somshine.taskmanager.dto.UserTaskDto;
import com.somshine.taskmanager.entity.TaskStatue;
import com.somshine.taskmanager.entity.UserTask;
import com.somshine.taskmanager.model.request.AssignTaskRequest;
import com.somshine.taskmanager.model.request.ProgressTaskRequest;
import com.somshine.taskmanager.model.request.UserTaskRequest;
import com.somshine.taskmanager.model.response.BaseResponse;
import com.somshine.taskmanager.model.response.TaskStatisticResponse;
import com.somshine.taskmanager.model.response.UserTaskSaveOrUpdateResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface UserTaskService {
    UserTaskSaveOrUpdateResponse save(UserTaskRequest request);
    void delete(Long taskId);
    UserTask getTaskById(Long Id);
    Page<UserTask> getAllTask(Integer pageNo, Integer pageSize);
    BaseResponse assignTaskToUser(AssignTaskRequest request);
    Page<UserTask> getUserAssignedTasks(Long userId, Integer pageNo, Integer pageSize);

    BaseResponse progressTask(ProgressTaskRequest request);

    Page<UserTask> getOverdueTasks(Integer pageNo, Integer pageSize);
    Page<UserTask> getTaskByStatus(String status, Integer pageNo, Integer pageSize);

    Page<UserTask> getTasksCompletedByDateRange(String status, String startDate, String endDate, Integer pageNo, Integer pageSize);

    TaskStatisticResponse getTaskStatistics();
    List<UserTask> getPriorityQueue();
}
