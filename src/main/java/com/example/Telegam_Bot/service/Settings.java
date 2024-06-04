package com.example.Telegam_Bot.service;

import com.example.Telegam_Bot.comands.BtnCommand;
import com.example.Telegam_Bot.entity.Email;
import com.example.Telegam_Bot.repository.EmailRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class Settings {
    EmailDetails emailDetails;

    public Settings(EmailDetails emailDetails) {
        this.emailDetails = emailDetails;
    }

    public EditMessageText showAllSettings(Integer messageId, Long chatId){
        var email = InlineKeyboardButton.builder()
                .text("")
                .callbackData("")
                .build();
        if (emailDetails.existEmailByChatId(chatId)){
            email.setText("Изменить почту \uD83D\uDCEC");
            email.setCallbackData(BtnCommand.CHANGE_EMAIL.getCommand());
        }
        else{
            email.setText("Добавить почту \uD83D\uDCEC");
            email.setCallbackData(BtnCommand.SET_EMAIL.getCommand());
        }
        var btnMarkup = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(email))
                .build();
        return EditMessageText.builder()
                .text("Настройки \uD83D\uDD27")
                .parseMode("HTML")
                .replyMarkup(btnMarkup)
                .messageId(messageId)
                .chatId(chatId)
                .build();
    }
    public EditMessageText setEmail(Integer messageId, Long chatId){
        String textMessage = "Введите почту на которую будут приходить сообщения о задачах: ";
        MySendMessage.statusChatUser.put(chatId, 8);
        return EditMessageText.builder()
                .text(textMessage)
                .messageId(messageId)
                .chatId(chatId)
                .build();
    }
    public SendMessage setEmail(Long chatId, String textMessage){
        if (!checkInvalidEmailString(textMessage)){
            return SendMessage.builder()
                    .text("Почта неправильная, попробуйте еще раз или вернитесь в начало - /start")
                    .build();
        }
        emailDetails.saveEmail(new Email(chatId, textMessage));
        MySendMessage.statusChatUser.remove(chatId);
        return SendMessage.builder()
                .text("Почта успешно добавлена, вернуться в начало - /start")
                .chatId(chatId)
                .build();
    }
    boolean checkInvalidEmailString(String email){
        final String EMAIL_PATTERN =
                "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
