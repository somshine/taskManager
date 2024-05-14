package com.somshine.taskmanager.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Data
@Entity
@Table(name = "TASK_STATUES")
public class TaskStatue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NAME", nullable = false, length = 600)
    private String name;

    @Column(name = "DESCRIPTION", length = 3000)
    private String description;

    @ColumnDefault("1")
    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean isActive = false;

    @Column(name = "CREATED_ON")
    private Instant createdOn;

}