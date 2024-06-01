package com.example.Telegam_Bot;

import com.example.Telegam_Bot.config.BotConfig;
import com.example.Telegam_Bot.config.BotInitializer;
import com.example.Telegam_Bot.service.MySendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class MyTelegramBot extends TelegramLongPollingBot {

    final BotConfig botConfig;
    final MySendMessage mySendMessage;

    MyTelegramBot(BotConfig botConfig, MySendMessage mySendMessage) {
        this.botConfig = botConfig;
        this.mySendMessage = mySendMessage;
    }

    @Override
    public String getBotUsername() {
//        System.out.println("dfsf");
        return botConfig.getUsername();
    }

    @Override
    public String getBotToken() {
//        System.out.println("token");
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
//            System.out.println(update.getMessage().getText());
            SendMessage message = mySendMessage.sendMessage(update.getMessage());
            try {
                execute(message);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            int messageId = update.getCallbackQuery().getMessage().getMessageId();

            if (callbackData.equals("test")) {
                EditMessageText newMessage = new EditMessageText();
                newMessage.setChatId(String.valueOf(chatId));
                newMessage.setMessageId(messageId);
                newMessage.setText("Кнопка была нажата!");

                try {
                    execute(newMessage);
                } catch (TelegramApiException e) {
                    System.err.println("Error occurred: "+ e.getMessage());
                }
            }
        }

    }
}
