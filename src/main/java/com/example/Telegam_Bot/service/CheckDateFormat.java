package com.example.Telegam_Bot.service;

import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CheckDateFormat {

    public boolean isValidTime(String time) {
        final String TIME_REGEX = "([01]\\d|2[0-3]| \\d):[0-5]\\d";
        final Pattern TIME_PATTERN = Pattern.compile(TIME_REGEX);
        Matcher matcher = TIME_PATTERN.matcher(time);
        return matcher.matches();
    }

    public boolean isValidDay(String day) {
        String format = "dd.MM.yyyy";
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            formatter.parse(day);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
