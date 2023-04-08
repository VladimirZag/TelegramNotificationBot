package BotConfig;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.InlineQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.InlineQueryResultArticle;
import com.pengrad.telegrambot.request.AnswerInlineQuery;
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
        InlineQuery inlineQuery = update.inlineQuery();

        BaseRequest request = null;

        if (inlineQuery != null) {
            InlineQueryResultArticle inlineCreate =
                    new InlineQueryResultArticle("create", "Создать", "Создание уведомления");
            InlineQueryResultArticle inlineEdit =
                    new InlineQueryResultArticle("edit", "Редактировать", "Редактировать уведомление");
            InlineQueryResultArticle inlineDelete =
                    new InlineQueryResultArticle("delete", "Удалить", " Удалить уведомление");
            InlineQueryResultArticle inlineHelp =
                    new InlineQueryResultArticle("help", "Помощь", "Справка");
            request = new AnswerInlineQuery(inlineQuery.id(), inlineCreate, inlineEdit, inlineDelete, inlineHelp);


        } else if (message != null) {
            long chatId = message.chat().id();
            
            request = new SendMessage(chatId, "Hello " + update.message().chat().firstName() + "!");
            switch (message.text()) {
                case ("/start"): {
                    request = new SendMessage(chatId, "Добавьте бот в нужный чат. Вызовите через '@' id бота и выберите нужное действие. Для подробной информации нажмите команду 'Помощь'");
                    break;
                }
            }
        }

        if (request != null) {
            bot.execute(request);
        }

    }
}
