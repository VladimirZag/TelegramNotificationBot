package BotConfig;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class BotService {

    static String notificationText = "Нет текста\uD83D\uDCE6";
    static String notificationTime = "Время не установлено\uD83D\uDD50";

    static final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BotConfig.class);
    private static final BotConfig botConfig = context.getBean(BotConfig.class);
    static final TelegramBot bot = new TelegramBot(botConfig.getBotKey());

    public void botStart() {

        bot.setUpdatesListener(updates -> {
            updates.forEach(this::process);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    public void process(Update update) {
        Message message = update.message();
        CallbackQuery callbackQuery = update.callbackQuery();
        InlineQuery inlineQuery = update.inlineQuery();

        BaseRequest request = null;

        if (message != null && message.viaBot() != null && message.viaBot().username().equals("Notifications1XklmrBots1_bot")) {
            Long chatId = message.chat().id();
            switch (message.text()) {
                case ("Создание уведомления\uD83E\uDDE9"): {
                    request = new SendMessage(chatId, "Введите текст уведомления начиная с '/t'✍️и время уведомления начиная с '/d' в другом сообщении.\n" +
                            " Образец ввода:\uD83D\uDC47\n" +
                            " '/t Hello!' и '/d 12:00'").replyMarkup(new InlineKeyboardMarkup(
                            new InlineKeyboardButton("Показать текущий текст").callbackData("0"), new InlineKeyboardButton("Показать текущее время").callbackData("1")));
                    DoRequest(request);
                    break;
                }
                case ("Уведомление удалено.✅"): {
                    notificationText = "Нет текста\uD83D\uDCE6";
                    notificationTime = "Время не установлено\uD83D\uDD50";
                    break;
                }
                case ("\uD83D\uDCD6\uD83D\uDD27Вызовите через '@' id бота и выберите нужное действие.\n " +
                        "1. 'Создать уведомление✅' - описывает создание нового уведомления, так же позволяет посмотреть уже установленное уведомление.\n " +
                        "2. 'Удалить уведомление❌' - очищает Ваше уведомление."): {
                    request = new SendPhoto(chatId, "https://disk.yandex.ru/d/qk_tiiI9vOx37w");
                    DoRequest(request);
                    break;
                }
            }
        }
        if (inlineQuery != null) {
            InlineQueryResultArticle inlineCreate =
                    new InlineQueryResultArticle("create", "Создать уведомление✅", "Создание уведомления\uD83E\uDDE9");
            InlineQueryResultArticle inlineDelete =
                    new InlineQueryResultArticle("delete", "Удалить уведомление❌", "Уведомление удалено.✅");
            InlineQueryResultArticle inlineHelp =
                    new InlineQueryResultArticle("help", "Помощь\uD83E\uDDD0", "\uD83D\uDCD6\uD83D\uDD27Вызовите через '@' id бота и выберите нужное действие.\n " +
                            "1. 'Создать уведомление✅' - описывает создание нового уведомления, так же позволяет посмотреть уже установленное уведомление.\n " +
                            "2. 'Удалить уведомление❌' - очищает Ваше уведомление.");
            request = new AnswerInlineQuery(inlineQuery.id(), inlineCreate, inlineDelete, inlineHelp).cacheTime(1);
            DoRequest(request);


        } else if (message != null) {

            long chatId = message.chat().id();
            if (message.text() != null && "/start".equals(message.text()) || "/start@Notifications1XklmrBots1_bot".equals(message.text())) {
                request = new SendMessage(chatId, "\uD83D\uDCD6Добавьте бот в нужный чат. Если бот уже добавлен, вызовите через '@' id бота и выберите нужное действие.");
                DoRequest(request);
            }
            if (message.text() != null && "/t ".equals(message.text().substring(0, 3))) {
                notificationText = message.text().substring(3);
                request = new SendMessage(chatId, "Текст уведомления сохранен!✅");
                DoRequest(request);
            }
            if (message.text() != null && "/d ".equals(message.text().substring(0, 3))) {
                notificationTime = message.text().substring(3);
                new Thread(new NotificationProcessor(notificationTime, update)).start();
                request = new SendMessage(chatId, "Время уведомления сохранено!✅");
                DoRequest(request);
            }
            if (update.message().newChatMembers() != null && Arrays.stream(update.message().newChatMembers()).findFirst().get().username().equals("Notifications1XklmrBots1_bot")) {
                request = new SendMessage(chatId, "Привет\uD83D\uDC4B, " + message.from().firstName() + "! Бот активирован.\uD83E\uDD16 Введите команду /start");
                DoRequest(request);
            }
        } else if (callbackQuery != null) {
            Long chatId = callbackQuery.message().chat().id();
            switch (callbackQuery.data()) {
                case ("0"): {
                    request = new SendMessage(chatId, "\uD83D\uDCD6Ваш текст уведомления: \n" + notificationText);
                    DoRequest(request);
                    break;
                }
                case ("1"): {
                    request = new SendMessage(chatId, "\uD83D\uDD50Заданное время уведомления: \n" + notificationTime);
                    DoRequest(request);
                    break;
                }
            }
        }

    }

    public static void TextMethod(Update update) {
        BaseRequest request = null;
        Message message = update.message();
        if (message != null) {
            request = new SendMessage(update.message().chat().id(), "❗\n" + notificationText);
            DoRequest(request);
        }
    }

    public static void DoRequest(BaseRequest request) {
        if (request != null) {
            bot.execute(request);

        }
    }
}