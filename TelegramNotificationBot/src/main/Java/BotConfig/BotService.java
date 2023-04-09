package BotConfig;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.InlineQueryResultArticle;
import com.pengrad.telegrambot.request.AnswerInlineQuery;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
                    new InlineQueryResultArticle("help", "Помощь", "/start");
            request = new AnswerInlineQuery(inlineQuery.id(), inlineCreate, inlineEdit, inlineDelete, inlineHelp);
            DoRequest(request);


        } else if (message != null) {
            long chatId = message.chat().id();
            if ("/start".equals(message.text())) {
                request = new SendMessage(chatId, "Привет, " + message.from().firstName() + "! Добавьте бот в нужный чат и назначьте его администратором. Вызовите через '@' id бота и выберите нужное действие.");
                DoRequest(request);
            }
        }
//        if (update.message().chat().joinByRequest() != null && message != null) {
//            request = new SendMessage(message.chat().id(), "Привет! Введите команду '/start'");
//            DoRequest(request);
//        }

    }

    public void DoRequest(BaseRequest request) {
        if (request != null) {
            bot.execute(request);
        }
    }
}
