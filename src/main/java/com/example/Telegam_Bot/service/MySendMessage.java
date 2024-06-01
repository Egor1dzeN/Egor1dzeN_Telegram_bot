package com.example.Telegam_Bot.service;

import com.example.Telegam_Bot.comands.MessageComand;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Service
@Data
public class MySendMessage {
    public SendMessage sendMessage(Message message) {
        String messageText = message.getText();
        if (messageText.equals(MessageComand.START.getString())) {
            return startMessage(message.getChatId());
        }
        return null;
    }

    public SendMessage startMessage(Long chatID) {
        String messageStartText = "Привет! Этот бот \uD83E\uDD16 был создан, чтобы мы с тобой" +
                " ничего не забыли сделать ✅ Вот доступные команды бота:";
        var createNewTask = InlineKeyboardButton.builder()
                .text("Добавить новую задачу ✎")
                .callbackData("create_new_task")
                .build();
        var myTasks = InlineKeyboardButton.builder()
                .text("Мои задачи \uD83D\uDCD6")
                .callbackData("my_tasks")
                .build();
        var btnsList = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(createNewTask))
                .keyboardRow(List.of(myTasks))
                .build();
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatID)
                .parseMode("HTML")
                .text(messageStartText)
                .replyMarkup(btnsList)
                .build();
        return sendMessage;
    }
}
