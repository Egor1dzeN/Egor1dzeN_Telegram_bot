package com.example.Telegam_Bot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@Configuration
//@Scope(value = "singleton")
//@ConfigurationProperties(prefix = "telegram.bot")
public class BotConfig {
    @Value("${telegram.bot.username}")
    String username;
    @Value("${telegram.bot.token}")
    String token;
}
