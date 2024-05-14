package com.somshine.taskmanager.repository;

import com.somshine.taskmanager.config.TaskStatuses;
import com.somshine.taskmanager.entity.TaskStatue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskStatusRepository extends JpaRepository<TaskStatue, Long> {
    Optional<TaskStatue> findByNameIgnoreCase(final String name);
}
