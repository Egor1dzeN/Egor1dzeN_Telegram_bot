package com.example.Telegam_Bot.repository;

import com.example.Telegam_Bot.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUserId(Long id);
}
