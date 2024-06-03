package com.example.Telegam_Bot.service;

import com.example.Telegam_Bot.comands.BtnCommand;
import com.example.Telegam_Bot.entity.Task;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
public class MyCallbackQueryHandler {
    public EditMessageText sendMessageBtn(CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();
        if (data.equals(BtnCommand.NEW_TASK.getCommand())) {
            return createNewTask(callbackQuery.getMessage().getMessageId(), callbackQuery.getMessage().getChatId());
        }
        if (data.equals(BtnCommand.CREATE_TASK_TODAY.getCommand())){
            return creatTaskToday(callbackQuery.getMessage().getMessageId(), callbackQuery.getMessage().getChatId());
        }
        return null;
    }

    public EditMessageText createNewTask(Integer messageId, Long chatId) {
        var todayBtn = InlineKeyboardButton.builder()
                .text("⏱\uFE0F Сегодня").callbackData(BtnCommand.CREATE_TASK_TODAY.getCommand())
                .build();
        var tomorrowBtn = InlineKeyboardButton.builder()
                .text("⏱\uFE0F Завтра").callbackData(BtnCommand.CREATE_TASK_TOMORROW.getCommand())
                .build();
        var otherTimeBtn = InlineKeyboardButton.builder()
                .text("ℹ Другое время").callbackData(BtnCommand.CREATE_TASK_TOMORROW.getCommand())
                .build();
        var btnsList = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(todayBtn))
                .keyboardRow(List.of(tomorrowBtn))
                .keyboardRow(List.of(otherTimeBtn))
                .build();
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .parseMode("HTML")
                .text("Когда вы хотите добавить новую задачу?\uD83D\uDCC5")
                .replyMarkup(btnsList)
                .build();
    }
    public EditMessageText creatTaskToday(Integer messageId, Long chatId){
        String textMessage = "Введите время⏱\uFE0F, на которое хотите добавить новую задачу в формате чч:мм";
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(textMessage)
                .build();
    }
}
