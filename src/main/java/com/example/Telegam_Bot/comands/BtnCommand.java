package com.example.Telegam_Bot.comands;

import lombok.Getter;

@Getter
public enum BtnCommand {
    NEW_TASK("create_new_task"),
    MY_TASKS("my_tasks"),
    CREATE_TASK_TODAY("create_task_today"),
    CREATE_TASK_TOMORROW("create_task_today"),
    CREATE_TASK_OTHER_TIME("create_task_other_time");
    final String command;

    BtnCommand(String command){
        this.command = command;
    }
}
