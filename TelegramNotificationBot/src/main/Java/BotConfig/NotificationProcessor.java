package BotConfig;

import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Queue;

public class NotificationProcessor implements Runnable{
 // private   Queue<String> queue;
 // private Fu
    private final String time;

    public NotificationProcessor(String time) {
        this.time = time;
    }

    @Override
    public void run() {
        while (true){
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                String localTime = formatter.format(LocalTime.now());
                if (time.equals(localTime)){
                    System.out.println("gg");
                }
                Thread.sleep(60000);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
