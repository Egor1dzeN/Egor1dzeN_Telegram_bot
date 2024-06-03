package com.example.Telegam_Bot.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.sql.Time;
import java.util.Date;

@Entity(name = "Task")
@Data
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NonNull
    Long userId;
    @Temporal(TemporalType.DATE)
    Date day;
    @Temporal(TemporalType.TIME)
    Date time;
    @NonNull
    String task;


    public Task() {

    }
}
