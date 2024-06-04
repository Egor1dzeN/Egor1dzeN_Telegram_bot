package com.example.Telegam_Bot.service;

import com.example.Telegam_Bot.comands.BtnCommand;
import com.example.Telegam_Bot.comands.MessageComand;
import com.example.Telegam_Bot.entity.Task;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Data
public class MySendMessage {
    @Autowired
    CheckDateFormat checkDateFormat;
    @Autowired
    TasksDetails tasksDetails;

    public static HashMap<Long, Task> nonCreatedTask = new HashMap<>();
    //0 - nothing, 1 - set day, 2 - set time, 3 - set comment, 4 - set N task for change
    public static HashMap<Long, Integer> statusChatUser = new HashMap<>();
    public static HashMap<Long, Integer> N_taskForChange = new HashMap<>();

    public SendMessage sendMessage(Message message) {
        String messageText = message.getText();
        if (messageText.equals(MessageComand.START.getCommand())) {
            return startMessage(message.getChatId());
        } else if (messageText.equals(MessageComand.SKIP.getCommand())) {
            return setTime(message.getChatId(), "00:00");
        } else if (statusChatUser.getOrDefault(message.getChatId(), 0) == 1) {
            return setDay(message.getChatId(), messageText);
        } else if (statusChatUser.getOrDefault(message.getChatId(), 0) == 2) {
            return setTime(message.getChatId(), messageText);
        } else if (statusChatUser.getOrDefault(message.getChatId(), 0) == 3) {
            return setComment(message.getChatId(), messageText);
        }else if (statusChatUser.getOrDefault(message.getChatId(), 0) == 4) {
            return setTaskForChange(message.getChatId(), messageText);
        }
        else if (statusChatUser.getOrDefault(message.getChatId(), 0) == 5) {
            return setDayTask(message.getChatId(), messageText);
        }
        else if (statusChatUser.getOrDefault(message.getChatId(), 0) == 6) {
            return setTimeTask(message.getChatId(), messageText);
        }
        else if (statusChatUser.getOrDefault(message.getChatId(), 0) == 7) {
            return setCommentTask(message.getChatId(), messageText);
        }

        return SendMessage.builder().text("Я вас не понял(, вернуться в начало - /start").chatId(message.getChatId()).build();
    }

    public SendMessage startMessage(Long chatID) {
        statusChatUser.put(chatID, 0);
        String messageStartText = "Привет! Этот бот \uD83E\uDD16 был создан, чтобы мы с тобой" +
                " ничего не забыли сделать ✅ Вот доступные команды бота:";
        var createNewTask = InlineKeyboardButton.builder()
                .text("Добавить новую задачу \uD83D\uDFE2")
                .callbackData(BtnCommand.NEW_TASK.getCommand())
                .build();
        var changeTask = InlineKeyboardButton.builder()
                .text("Изменить задачу ✎")
                .callbackData(BtnCommand.CHANGE_TASK.getCommand())
                .build();
        var myTasks = InlineKeyboardButton.builder()
                .text("Мои задачи \uD83D\uDCD6")
                .callbackData(BtnCommand.MY_TASKS.getCommand())
                .build();
        var btnsList = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(createNewTask))
                .keyboardRow(List.of(changeTask))
                .keyboardRow(List.of(myTasks))
                .build();
        return SendMessage.builder()
                .chatId(chatID)
                .parseMode("HTML")
                .text(messageStartText)
                .replyMarkup(btnsList)
                .build();
    }

    public SendMessage setDay(Long chatId, String messageText) {
        String textAnswer = "";
        String format = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date();
        try {
            date = sdf.parse(messageText);
        } catch (ParseException e) {
            textAnswer = "Ошибка при обработке даты :( Попробуйте ввести еще раз, в формате дд.мм.гггг," +
                    " Например: 17 сентября 2024 года будет выглядеть так: 17.09.2024";
            return SendMessage.builder()
                    .text(textAnswer)
                    .chatId(chatId)
                    .build();
        }
        Task task = new Task(chatId, date);
        nonCreatedTask.put(chatId, task);
        statusChatUser.put(chatId, 2);
        String textMessage = "Введите время⏱\uFE0F, на которое хотите добавить новую задачу в формате чч:мм \uD83E\uDDFE" +
                " или введите /skip, чтобы оставить только дату";
        return SendMessage.builder()
                .chatId(chatId)
                .text(textMessage)
                .build();
    }

    public SendMessage setTime(Long chatId, String messageText) {
        String textAnswer = "";
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setLenient(false);
        Date time = new Date();
        try {
            time = sdf.parse(messageText);

        } catch (Exception e) {
            textAnswer = "Ошибка при обработке времени :( Попробуйте ввести еще раз, в формате чч:мм";
            return SendMessage.builder()
                    .text(textAnswer)
                    .chatId(chatId)
                    .build();
        }
        textAnswer = "Введите комментарий к вашему делу: ";
        Task task = nonCreatedTask.getOrDefault(chatId, new Task());
        task.setTime(time);
        statusChatUser.put(chatId, 3);
        return SendMessage.builder()
                .text(textAnswer)
                .chatId(chatId)
                .build();
    }

    public SendMessage setComment(Long chatId, String messageText) {
        Task task = nonCreatedTask.getOrDefault(chatId, new Task());
        task.setTaskMessage(messageText);
        tasksDetails.saveTask(task);
        nonCreatedTask.remove(chatId);
        statusChatUser.remove(chatId);
        return SendMessage.builder()
                .text("Задача была успешно создана!) ✔\uFE0F Введите /start")
                .chatId(chatId)
                .build();
    }

    public SendMessage setTaskForChange(Long chatId, String messageText) {
        final String DIGIT_REGEX = "\\d";
        final Pattern DIGIT_PATTERN = Pattern.compile(DIGIT_REGEX);
        Matcher matcher = DIGIT_PATTERN.matcher(messageText);
        if (!matcher.matches()) {
            return SendMessage.builder()
                    .text("Вы ввели не число :(, попробуйте еще раз или введите /start, чтобы вернуться в начало")
                    .chatId(chatId)
                    .build();
        }
        List<Task> listTask = tasksDetails.getAllTasksByUserId(chatId);
        if (Integer.parseInt(messageText) > listTask.size() || Integer.parseInt(messageText) < 1) {
            return SendMessage.builder()
                    .text("Вы ввели число не соответсвующее номеру какой-либо задачи" +
                            " :(, попробуйте еще раз или введите /start, чтобы вернуться в начало")
                    .chatId(chatId)
                    .build();
        }
        statusChatUser.put(chatId, 5);
        N_taskForChange.put(chatId, Integer.valueOf(messageText));
        var changeDay = InlineKeyboardButton.builder()
                .text("Изменить день")
                .callbackData(BtnCommand.CHANGE_DAY_TASK.getCommand())
                .build();
        var changeTime = InlineKeyboardButton.builder()
                .text("Изменить время")
                .callbackData(BtnCommand.CHANGE_TIME_TASK.getCommand())
                .build();
        var changeComment = InlineKeyboardButton.builder()
                .text("Изменить комментарий")
                .callbackData(BtnCommand.CHANGE_COMMENT_TASK.getCommand())
                .build();
        var btnMarkup = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(changeDay))
                .keyboardRow(List.of(changeTime))
                .keyboardRow(List.of(changeComment)).build();
        return SendMessage.builder()
                .chatId(chatId)
                .text("Выберите, что вы хотите изменить в задаче:")
                .replyMarkup(btnMarkup)
                .parseMode("HTML")
                .build();
    }

    public SendMessage setDayTask(Long chatId, String messageText) {
        String textAnswer;
        String format = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date();
        try {
            date = sdf.parse(messageText);
        } catch (ParseException e) {
            textAnswer = "Ошибка при обработке даты :( Попробуйте ввести еще раз, в формате дд.мм.гггг," +
                    " Например: 17 сентября 2024 года будет выглядеть так: 17.09.2024";
            return SendMessage.builder()
                    .text(textAnswer)
                    .chatId(chatId)
                    .build();
        }
        Task task = new Task(chatId, date);
        statusChatUser.remove(chatId);
        tasksDetails.changeDayTask(N_taskForChange.get(chatId), chatId, date);
        String textMessage = "Задача была успешно изменена!) ✔️ Введите /start";
        return SendMessage.builder()
                .chatId(chatId)
                .text(textMessage)
                .build();
    }
    public SendMessage setTimeTask(Long chatId, String messageText) {
        String textAnswer = "";
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setLenient(false);
        Date time = new Date();
        try {
            time = sdf.parse(messageText);

        } catch (Exception e) {
            textAnswer = "Ошибка при обработке времени :( Попробуйте ввести еще раз, в формате чч:мм";
            return SendMessage.builder()
                    .text(textAnswer)
                    .chatId(chatId)
                    .build();
        }
        textAnswer = "Задача была успешно изменена!) ✔️ Введите /start";
        tasksDetails.changeTimeTask(N_taskForChange.get(chatId), chatId, time);
        statusChatUser.remove(chatId);
        return SendMessage.builder()
                .text(textAnswer)
                .chatId(chatId)
                .build();
    }
    public SendMessage setCommentTask(Long chatId, String messageText) {
        tasksDetails.changeCommentTask(N_taskForChange.get(chatId), chatId, messageText);
        String textAnswer = "Задача была успешно изменена!) ✔️ Введите /start";
        statusChatUser.remove(chatId);
        return SendMessage.builder()
                .text(textAnswer)
                .chatId(chatId)
                .build();
    }
}
