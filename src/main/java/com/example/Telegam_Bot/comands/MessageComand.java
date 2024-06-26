package com.example.Telegam_Bot.comands;


import lombok.Getter;

@Getter
public enum MessageComand {
    START("/start"),
    HELP("/help"),
    SKIP("/skip");
    final String command;

    MessageComand(String command) {
        this.command = command;
    }
}
