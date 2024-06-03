package com.example.Telegam_Bot.service;

import com.example.Telegam_Bot.comands.BtnCommand;
import com.example.Telegam_Bot.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class MyCallbackQueryHandler {

    @Autowired
    TasksDetails tasksDetails;

    public EditMessageText sendMessageBtn(CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();
        if (data.equals(BtnCommand.NEW_TASK.getCommand())) {
            return createNewTask(callbackQuery.getMessage().getMessageId(), callbackQuery.getMessage().getChatId());
        }
//        System.out.println(data);
        if (data.equals(BtnCommand.CREATE_TASK_TODAY.getCommand())) {
            return createTaskToday(callbackQuery.getMessage().getMessageId(), callbackQuery.getMessage().getChatId());
        }
        if (data.equals(BtnCommand.CREATE_TASK_TOMORROW.getCommand())) {
            return createTaskTomorrow(callbackQuery.getMessage().getMessageId(), callbackQuery.getMessage().getChatId());
        }
        if (data.equals(BtnCommand.CREATE_TASK_OTHER_TIME.getCommand())) {
            return createTaskOtherDay(callbackQuery.getMessage().getMessageId(), callbackQuery.getMessage().getChatId());
        }
        if (data.equals(BtnCommand.MY_TASKS.getCommand())) {
            return showMyTasks(callbackQuery.getMessage().getMessageId(), callbackQuery.getMessage().getChatId());
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
                .text("ℹ Другое время").callbackData(BtnCommand.CREATE_TASK_OTHER_TIME.getCommand())
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

    public EditMessageText createTaskToday(Integer messageId, Long chatId) {
        Task task = new Task(chatId, new Date());
        MySendMessage.nonCreatedTask.put(chatId, task);
        MySendMessage.statusCreatingTask.put(chatId, 2);
        String textMessage = "Введите время⏱\uFE0F, на которое хотите добавить новую задачу в формате чч:мм или введите /-, чтобы оставить только дату";
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(textMessage)
                .build();
    }

    public EditMessageText createTaskTomorrow(Integer messageId, Long chatId) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date date_tomorrow = calendar.getTime();
        System.out.println(date_tomorrow);
        Task task = new Task(chatId, date_tomorrow);
        MySendMessage.nonCreatedTask.put(chatId, task);
        MySendMessage.statusCreatingTask.put(chatId, 2);
        String textMessage = "Введите время⏱\uFE0F, на которое хотите добавить новую задачу в формате чч:мм или введите /-, чтобы оставить только дату";
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(textMessage)
                .build();
    }

    public EditMessageText createTaskOtherDay(Integer messageId, Long chatId) {
        String textMessage = "Введите дату задачи в формате дд.мм.гггг," +
                " Например: 1 января 2024 года будет выглядеть так: 01.01.2024";
        MySendMessage.statusCreatingTask.put(chatId, 1);
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(textMessage)
                .build();
    }

    public EditMessageText showMyTasks(Integer messageId, Long chatId) {
        List<Task> listTasksByUserId = tasksDetails.getAllTasksByUserId(chatId);
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(listTasksByUserId.toString())
                .build();
    }
}
