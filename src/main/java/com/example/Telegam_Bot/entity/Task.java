package com.example.Telegam_Bot.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity(name = "Task")
@Data
//@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long userId;
    @Temporal(TemporalType.DATE)
    Date day;
    @Temporal(TemporalType.TIME)
    Date time;
    String taskMessage;

    public Task() {

    }
    public Task(Long userId){
        this.userId = userId;
    }
    public Task(Long userId, Date day){
        this.userId = userId;
        this.day = day;
    }
}
