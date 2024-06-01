package com.example.Telegam_Bot;

import com.example.Telegam_Bot.config.BotConfig;
import com.example.Telegam_Bot.config.BotInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class MyTelegramBot extends TelegramLongPollingBot {

    final BotConfig botConfig;

    MyTelegramBot(BotConfig botConfig) {
        this.botConfig = botConfig;
    }

    @Override
    public String getBotUsername() {
        System.out.println("dfsf");
        return botConfig.getUsername();
    }

    @Override
    public String getBotToken() {
        System.out.println("token");
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update);
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            long chatID = update.getMessage().getChatId();
            SendMessage messageAnswer = new SendMessage();
            messageAnswer.setChatId(chatID + "");
            messageAnswer.setText("You write: " + message);
            try {
                execute(messageAnswer);
            } catch (TelegramApiException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
