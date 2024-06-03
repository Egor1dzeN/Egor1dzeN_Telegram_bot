package com.example.Telegam_Bot.service;

import com.example.Telegam_Bot.entity.Task;
import com.example.Telegam_Bot.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TasksDetails {
    @Autowired
    TaskRepository taskRepository;
    public void saveTask(Task task){
        taskRepository.save(task);
    }
    public List<Task> getAllTasksByUserId(Long userId){
        return taskRepository.findAllByUserIdOrderByDayAsc(userId);
    }
}
