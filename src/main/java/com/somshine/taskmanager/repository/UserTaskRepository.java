package com.somshine.taskmanager.repository;

import com.somshine.taskmanager.dto.UserTaskDto;
import com.somshine.taskmanager.entity.TaskStatue;
import com.somshine.taskmanager.entity.User;
import com.somshine.taskmanager.entity.UserTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface UserTaskRepository extends JpaRepository<UserTask, Long> {
    Page<UserTask> findUserTaskByTitleIsNotNull(Pageable pageable);
    Page<UserTask> findUserTaskByUser(User user, Pageable pageable);

    Page<UserTask> findUserTaskByDueDateIsLessThan(LocalDateTime dueDate, Pageable pageable);
    Page<UserTask> findUserTaskByTaskStatus(TaskStatue taskStatue, Pageable pageable);

    Page<UserTask> findUserTaskByTaskStatusAndCreatedOnBetween(TaskStatue taskStatue, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Long countByTaskStatus(TaskStatue taskStatue);
}
