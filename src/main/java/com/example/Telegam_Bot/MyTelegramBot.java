package com.example.Telegam_Bot;

import com.example.Telegam_Bot.config.BotConfig;
import com.example.Telegam_Bot.service.MyCallbackQueryHandler;
import com.example.Telegam_Bot.service.MySendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class MyTelegramBot extends TelegramLongPollingBot {

    final BotConfig botConfig;
    final MySendMessage mySendMessage;
    final MyCallbackQueryHandler myCallbackQueryHandler;

    @Autowired
    MyTelegramBot(BotConfig botConfig, MySendMessage mySendMessage, MyCallbackQueryHandler myCallbackQueryHandler) {
        this.botConfig = botConfig;
        this.mySendMessage = mySendMessage;
        this.myCallbackQueryHandler = myCallbackQueryHandler;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getUsername();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = mySendMessage.sendMessage(update.getMessage());
            try {
                execute(message);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            EditMessageText editMessageText = myCallbackQueryHandler.sendMessageBtn(callbackQuery);
            try {
                execute(editMessageText);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
