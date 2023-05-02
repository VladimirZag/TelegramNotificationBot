package BotConfig;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import jdk.nashorn.internal.objects.annotations.Function;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Queue;

public class NotificationProcessor implements Runnable {
    private final String time;
    private final Update update;

    public NotificationProcessor(String time, Update update) {
        this.time = time;
        this.update = update;
    }

    @Override
    public void run() {
        while (true) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                String localTime = formatter.format(LocalTime.now());
                if (time.equals(localTime)) {
                    BotService.TextMethod(update);
                }
                Thread.sleep(60000);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
