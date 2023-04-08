package BotConfig;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

public class BotService {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BotConfig.class);
    private final BotConfig botConfig = context.getBean(BotConfig.class);
    private final TelegramBot bot = new TelegramBot(botConfig.getBotKey());

    public void botStart() {
        bot.setUpdatesListener(updates -> {
            updates.forEach(this::process);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    public void process(Update update) {
        Message message = update.message();
        CallbackQuery callbackQuery = new CallbackQuery();

        BaseRequest request = null;

        if (message != null) {
            long chatId = message.chat().id();
            request = new SendMessage(chatId, "Hello " + update.message().chat().firstName() + "!");
            switch (message.text()) {
                case ("/start"): {
                    request = new SendMessage(chatId, "Добавьте бот в нужный чат.");
                }
            }

            if (request != null) {
                bot.execute(request);
            }
        }

    }
}
