package com.example.Telegam_Bot.comands;

import lombok.Getter;

@Getter
public enum BtnCommand {
    NEW_TASK("create_new_task"),
    MY_TASKS("my_tasks"),
    CREATE_TASK_TODAY("create_task_today"),
    CREATE_TASK_TOMORROW("create_task_tomorrow"),
    CREATE_TASK_OTHER_TIME("create_task_other_time"),
    CHANGE_TASK("/skip"),
    CHANGE_TIME_TASK("change_time_task"),
    CHANGE_DAY_TASK("change_day_task"),
    CHANGE_COMMENT_TASK("change_comment_task"),
    DELETE_TASK("delete_task");
    final String command;

    BtnCommand(String command){
        this.command = command;
    }
}
