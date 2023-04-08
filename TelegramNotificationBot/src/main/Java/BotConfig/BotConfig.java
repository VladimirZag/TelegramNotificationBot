package BotConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan("BotConfig")
public class BotConfig {
    @Value("${bot.key}")
    private String botKey;

    public String getBotKey() {
        return botKey;
    }
}
