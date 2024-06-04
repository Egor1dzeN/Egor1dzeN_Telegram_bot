package com.example.Telegam_Bot.service;

import com.example.Telegam_Bot.comands.BtnCommand;
import com.example.Telegam_Bot.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
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
        if (data.equals(BtnCommand.CHANGE_TASK.getCommand())){
            return changeTask(callbackQuery.getMessage().getMessageId(), callbackQuery.getMessage().getChatId());
        }
        if (data.equals(BtnCommand.CHANGE_DAY_TASK.getCommand())){

        }
        return EditMessageText.builder()
                .text("))")
                .chatId(data)
                .messageId(callbackQuery.getMessage().getMessageId())
                .build();
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
        MySendMessage.statusChatUser.put(chatId, 2);
        String textMessage = "Введите время⏱\uFE0F, на которое хотите добавить новую задачу в формате чч:мм \uD83E\uDDFE или введите /skip, чтобы оставить только дату";
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
        MySendMessage.statusChatUser.put(chatId, 2);
        String textMessage = "Введите время⏱\uFE0F, на которое хотите добавить новую задачу в формате чч:мм \uD83E\uDDFE или введите /skip, чтобы оставить только дату";
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(textMessage)
                .build();
    }

    public EditMessageText createTaskOtherDay(Integer messageId, Long chatId) {
        String textMessage = "Введите дату задачи в формате дд.мм.гггг," +
                " Например: 1 января 2024 года будет выглядеть так: 01.01.2024";
        MySendMessage.statusChatUser.put(chatId, 1);
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(textMessage)
                .build();
    }

    public EditMessageText showMyTasks(Integer messageId, Long chatId) {
        List<Task> listTasksByUserId = tasksDetails.getAllTasksByUserId(chatId);
        StringBuilder tableBuilder = new StringBuilder();

        tableBuilder.append("N| Дата            | Время | Комментарий       \n");
        tableBuilder.append("-----------------------------------------------------------\n");
        for (int i = 0; i < listTasksByUserId.size(); ++i) {
            var task = listTasksByUserId.get(i);
            SimpleDateFormat formetter_time = new SimpleDateFormat("HH:mm");
            SimpleDateFormat formetter_day = new SimpleDateFormat("dd.MM.yyyy");
            tableBuilder.append((i+1) + " | " + formetter_day.format(task.getDay()) + " | " + formetter_time.format(task.getTime()) + "    | " + task.getTaskMessage()+"\n");
        }
        // Добавьте другие строки таблицы, если нужно

        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(tableBuilder.toString())
                .build();
    }
    public EditMessageText changeTask(Integer messageId, Long chatId) {
        EditMessageText message = showMyTasks(messageId, chatId);
        String textMessage = message.getText();
        textMessage += "Введите номер задачи, которую хотите изменить:";
        message.setText(textMessage);
        return message;
    }
    public EditMessageText changeDayTask(Integer messageId, Long chatId) {
        String textMessage = "Введите время⏱\uFE0F, на которое хотите добавить новую задачу в формате чч:мм \uD83E\uDDFE или введите /skip, чтобы оставить только дату";
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(textMessage)
                .build();
    }
}
