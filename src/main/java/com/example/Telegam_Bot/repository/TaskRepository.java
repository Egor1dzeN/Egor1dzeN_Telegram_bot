package com.example.Telegam_Bot.repository;

import com.example.Telegam_Bot.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUserId(Long userId);
    List<Task> findAllByUserIdOrderByDayAsc(Long userId);
}
