package com.example.Telegam_Bot.service;

import com.example.Telegam_Bot.comands.BtnCommand;
import com.example.Telegam_Bot.comands.MessageComand;
import com.example.Telegam_Bot.entity.Task;
import com.example.Telegam_Bot.repository.TaskRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@Data
public class MySendMessage {
    @Autowired
    CheckDateFormat checkDateFormat;
    @Autowired
    TaskRepository taskRepository;

    public static HashMap<Long, Task> nonCreatedTask = new HashMap<>();
    public static HashMap<Long, Integer> statusCreatingTask = new HashMap<>();

    public SendMessage sendMessage(Message message) {
        String messageText = message.getText();
        if (messageText.equals(MessageComand.START.getCommand())) {
            return startMessage(message.getChatId());
        } else if (statusCreatingTask.getOrDefault(message.getChatId(), 0) == 1) {
            return setDay(message.getChatId(), messageText);
        } else if (statusCreatingTask.getOrDefault(message.getChatId(), 0) == 2) {
            return setTime(message.getChatId(), messageText);
        } else if (statusCreatingTask.getOrDefault(message.getChatId(), 0) == 3) {
            return setComment(message.getChatId(), messageText);
        }

        return null;
    }

    public SendMessage startMessage(Long chatID) {
        String messageStartText = "Привет! Этот бот \uD83E\uDD16 был создан, чтобы мы с тобой" +
                " ничего не забыли сделать ✅ Вот доступные команды бота:";
        var createNewTask = InlineKeyboardButton.builder()
                .text("Добавить новую задачу ✎")
                .callbackData(BtnCommand.NEW_TASK.getCommand())
                .build();
        var myTasks = InlineKeyboardButton.builder()
                .text("Мои задачи \uD83D\uDCD6")
                .callbackData(BtnCommand.MY_TASKS.getCommand())
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

    public SendMessage setDay(Long chatId, String messageText) {
        return null;
    }

    public SendMessage setTime(Long chatId, String messageText) {
        String textAnswer = "";
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setLenient(false);
        Date time = new Date();
        try {
            time = sdf.parse(messageText);

        } catch (Exception e) {
            textAnswer = "Ошибка при обработке времени :( Нажмите /start чтобы вернуться в начало";
            return SendMessage.builder()
                    .text(textAnswer)
                    .chatId(chatId)
                    .build();
        }
        textAnswer = "Введите комментарий к вашему делу: ";
        Task task = nonCreatedTask.getOrDefault(chatId, new Task());
        task.setTime(time);
        statusCreatingTask.put(chatId, 3);
        return SendMessage.builder()
                .text(textAnswer)
                .chatId(chatId)
                .build();
    }

    public SendMessage setComment(Long chatId, String messageText) {
        Task task = nonCreatedTask.getOrDefault(chatId, new Task());
        task.setTaskMessage(messageText);
        taskRepository.save(task);
        nonCreatedTask.remove(chatId);
        return SendMessage.builder()
                .text("Задача была успешно создана!)")
                .chatId(chatId)
                .build();
    }
}
