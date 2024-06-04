package com.example.Telegam_Bot.service;

import com.example.Telegam_Bot.entity.Task;
import com.example.Telegam_Bot.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TasksDetails {
    @Autowired
    TaskRepository taskRepository;

    public void saveTask(Task task) {
        taskRepository.save(task);
    }
    public void deleteTask(Task task){
        taskRepository.delete(task);
    }
    public List<Task> getAllTasksByUserId(Long userId) {
        return taskRepository.findAllByUserIdOrderByDayAsc(userId);
    }

    public void changeDayTask(int number, Long chatId, Date date) {
        List<Task> listTask = getAllTasksByUserId(chatId);
        Task task= listTask.get(number - 1);
        task.setDay(date);
        saveTask(task);
    }
    public void changeTimeTask(int number, Long chatId, Date time) {
        List<Task> listTask = getAllTasksByUserId(chatId);
        Task task= listTask.get(number - 1);
        task.setTime(time);
        saveTask(task);
    }
    public void changeCommentTask(int number, Long chatId, String comment) {
        List<Task> listTask = getAllTasksByUserId(chatId);
        Task task= listTask.get(number - 1);
        task.setTaskMessage(comment);
        saveTask(task);
    }
    public void deleteTask(int number, Long chatId) {
        List<Task> listTask = getAllTasksByUserId(chatId);
        Task task= listTask.get(number - 1);
        deleteTask(task);
    }

}
