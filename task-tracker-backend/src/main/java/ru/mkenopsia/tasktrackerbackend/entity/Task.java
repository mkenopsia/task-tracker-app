package ru.mkenopsia.tasktrackerbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@Table(schema = "task_management", name = "t_task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "c_name")
    private String name;

    @Column(name = "c_description")
    private String description;

    @Column(name = "c_isdone")
    private Boolean isDone;

    @Column(name = "c_date")
    private ZonedDateTime date;

    @ManyToOne
    private User author;
}
