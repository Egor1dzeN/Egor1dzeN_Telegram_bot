package com.example.Telegam_Bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class TelegamBotApplication {

    public static void main(String[] args) throws TelegramApiException {

//		Api
        SpringApplication.run(TelegamBotApplication.class, args);

    }

}
