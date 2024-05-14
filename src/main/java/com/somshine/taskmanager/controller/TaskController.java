package com.somshine.taskmanager.controller;

import com.somshine.taskmanager.config.TaskStatuses;
import com.somshine.taskmanager.dto.UserTaskDto;
import com.somshine.taskmanager.entity.UserTask;
import com.somshine.taskmanager.model.request.AssignTaskRequest;
import com.somshine.taskmanager.model.request.ProgressTaskRequest;
import com.somshine.taskmanager.model.request.UserTaskRequest;
import com.somshine.taskmanager.model.response.BaseResponse;
import com.somshine.taskmanager.model.response.TaskStatisticResponse;
import com.somshine.taskmanager.model.response.UserTaskSaveOrUpdateResponse;
import com.somshine.taskmanager.service.UserTaskService;
import com.somshine.taskmanager.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private UserTaskService userTaskService;

    @PostMapping
    public UserTaskSaveOrUpdateResponse saveOrUpdateTask(
            @RequestBody UserTaskRequest request
    ) {
        request.setId(null);
        return userTaskService.save(request);
    }

    @PutMapping("/{taskId}")
    public UserTaskSaveOrUpdateResponse updateTask(
            @RequestBody UserTaskRequest request,
            @PathVariable Long taskId
    ) {
        request.setId(taskId);
        return userTaskService.save(request);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteUserTask(@PathVariable Long taskId) {
        userTaskService.delete(taskId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<UserTask>> getAllUserTasks(
        @RequestParam(value="pageNo", required = false) Integer pageNo,
        @RequestParam(value="pageSize", required = false) Integer pageSize
    ) {
        Page<UserTask> page = userTaskService.getAllTask(pageNo, pageSize);
        return ResponseEntity.ok().body(page);
    }

    @PostMapping("/{taskId}/assign")
    public ResponseEntity<BaseResponse> assignTask(
        @RequestBody AssignTaskRequest request,
        @PathVariable Long taskId
    ) {
        request.setTaskId(taskId);
        return ResponseEntity.ok().body(userTaskService.assignTaskToUser(request));
    }

    @PutMapping("/{taskId}/progress")
    public ResponseEntity<BaseResponse> assignTaskProgress(
            @RequestBody ProgressTaskRequest request,
            @PathVariable Long taskId
    ) {
        request.setTaskId(taskId);
        return ResponseEntity.ok().body(userTaskService.progressTask(request));
    }

    @GetMapping("/overdue")
    public ResponseEntity<Page<UserTask>> getOverdueTasks(
        @RequestParam(value="pageNo", required = false) Integer pageNo,
        @RequestParam(value="pageSize", required = false) Integer pageSize
    ) {
        Page<UserTask> page = userTaskService.getAllTask(pageNo, pageSize);
        return ResponseEntity.ok().body(page);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<UserTask>> getTasksByStatus(
            @PathVariable String status,
            @RequestParam(value="pageNo", required = false) Integer pageNo,
            @RequestParam(value="pageSize", required = false) Integer pageSize
    ) {
        Page<UserTask> page = userTaskService.getTaskByStatus(status, pageNo, pageSize);
        return ResponseEntity.ok().body(page);
    }

    @GetMapping("/completed")
    public ResponseEntity<Page<UserTask>> getTasksCompletedByDateRange(
            @RequestParam(value="pageNo", required = false) Integer pageNo,
            @RequestParam(value="pageSize", required = false) Integer pageSize,
            @RequestParam(value="startDate") String startDate,
            @RequestParam(value="endDate") String endDate
    ) {
        Page<UserTask> page = userTaskService.getTasksCompletedByDateRange(TaskStatuses.COMPLETED, startDate, endDate, pageNo, pageSize);
        return ResponseEntity.ok().body(page);
    }

    @GetMapping("/statistics")
    public ResponseEntity<TaskStatisticResponse> getStatistics() {
        TaskStatisticResponse taskStatistics = userTaskService.getTaskStatistics();
        return ResponseEntity.ok().body(taskStatistics);
    }

    @GetMapping("/priorityQueue")
    public ResponseEntity<List<UserTask>> getPriorityQueue() {
        List<UserTask> userTasks = userTaskService.getPriorityQueue();
        return ResponseEntity.ok().body(userTasks);
    }
}
