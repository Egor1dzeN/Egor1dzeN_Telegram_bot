package com.example.Telegam_Bot.entity;

import com.example.Telegam_Bot.repository.EmailRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long chatId;
    String email;

    public Email(Long chatId, String email) {
        this.chatId = chatId;
        this.email = email;
    }

    public Email() {

    }
}
