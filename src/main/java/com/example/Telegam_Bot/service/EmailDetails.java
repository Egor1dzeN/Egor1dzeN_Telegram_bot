package com.example.Telegam_Bot.service;

import com.example.Telegam_Bot.entity.Email;
import com.example.Telegam_Bot.repository.EmailRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailDetails {
    EmailRepository emailRepository;
    @Autowired
    EmailDetails(EmailRepository emailRepository){
        this.emailRepository = emailRepository;
    }
    public boolean existEmailByChatId(Long chatId){
        return emailRepository.existsAllByChatId(chatId);
    }
    public void saveEmail(Email email){
        emailRepository.save(email);
    }
}
