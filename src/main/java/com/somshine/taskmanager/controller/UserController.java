package com.somshine.taskmanager.controller;

import com.somshine.taskmanager.entity.UserTask;
import com.somshine.taskmanager.service.UserTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserTaskService userTaskService;

    @GetMapping("/{userId}/tasks")
    public ResponseEntity<Page<UserTask>> getListOfUserAssignedTasks(
        @PathVariable Long userId,
        @RequestParam(value="pageNo", required = false) Integer pageNo,
        @RequestParam(value="pageSize", required = false) Integer pageSize
    ) {
        Page<UserTask> page = userTaskService.getUserAssignedTasks(userId, pageNo, pageSize);
        return ResponseEntity.ok().body(page);
    }
}
