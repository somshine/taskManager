package com.somshine.taskmanager.repository;

import com.somshine.taskmanager.entity.TaskPriority;
import com.somshine.taskmanager.entity.TaskStatue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskPriorityRepository extends JpaRepository<TaskPriority, Long> {
    Optional<TaskPriority> findByNameIgnoreCase(final String name);
}
