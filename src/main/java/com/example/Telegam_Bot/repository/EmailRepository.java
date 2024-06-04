package com.example.Telegam_Bot.repository;

import com.example.Telegam_Bot.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long> {
    boolean existsAllByChatId(Long chatId);
}
