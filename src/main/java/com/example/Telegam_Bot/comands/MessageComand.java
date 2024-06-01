package com.example.Telegam_Bot.comands;


public enum MessageComand {
    START("/start"),
    HELP("/help");
    final String string;

    MessageComand(String value) {
        this.string = value;
    }
    public String getString(){
        return this.string;
    }
}
