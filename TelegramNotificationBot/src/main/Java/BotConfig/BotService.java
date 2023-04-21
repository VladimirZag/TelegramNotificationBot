package BotConfig;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.*;
import okhttp3.Dispatcher;
import okhttp3.OkHttp;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.*;

public class BotService {
    static String notificationText = "Нет текста \uD83D\uDCE6";
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
        CallbackQuery callbackQuery = update.callbackQuery();
        InlineQuery inlineQuery = update.inlineQuery();

        BaseRequest request = null;

        if (message != null && message.viaBot() != null && message.viaBot().username().equals("Notifications1XklmrBots1_bot")) {
            Long chatId = message.chat().id();
            switch (message.text()) {
                case ("Создание уведомления\uD83E\uDDE9"): {
                    request = new SendMessage(chatId, "Введите текст уведомления начиная с '/t'✍️и нажмите 'Сохранить текст'. Образец ввода указан ниже\uD83D\uDC47")
                            .replyMarkup(new InlineKeyboardMarkup(new InlineKeyboardButton("Сохранить текст").callbackData("0")));
                    DoRequest(request);
                   request = new SendPhoto(chatId, "https://disk.yandex.ru/i/Q7iX7VDjQrFAMg");
                    DoRequest(request);
                    break;
                }
                case ("Редактор уведомления\uD83D\uDD27:"): {
                    request = new SendMessage(chatId, "Нажмите 'Показать текущий текст' или  Введите текст уведомления начиная с '/t'✍️и нажмите 'Сохранить новый текст'").replyMarkup(new InlineKeyboardMarkup(
                            new InlineKeyboardButton("Показать текущий текст").callbackData("1"), new InlineKeyboardButton("Сохранить новый текст").callbackData("2")));
                    DoRequest(request);
                    break;
                }
                case ("Уведомление удалено.✅"): {
                    notificationText = "Нет текста \uD83D\uDCE6";
                    break;
                }
            }

        }
        if (inlineQuery != null) {
            InlineQueryResultArticle inlineCreate =
                    new InlineQueryResultArticle("create", "Создать уведомление✅", "Создание уведомления\uD83E\uDDE9");
            InlineQueryResultArticle inlineEdit =
                    new InlineQueryResultArticle("edit", "Редактировать уведомление\uD83D\uDD27", "Редактор уведомления\uD83D\uDD27:");
            InlineQueryResultArticle inlineDelete =
                    new InlineQueryResultArticle("delete", "Удалить уведомление❌", "Уведомление удалено.✅");
            InlineQueryResultArticle inlineHelp =
                    new InlineQueryResultArticle("help", "Помощь\uD83E\uDDD0", "\uD83D\uDCD6Вызовите через '@' id бота и выберите нужное действие. 1. 'Создать уведомление✅' - позволяет создать новое уведомление. " +
                            "2. 'Редактировать уведомление\uD83D\uDD27' - позволяет посмотреть текст текущего уведомления и ввести новый, 3. 'Удалить уведомление❌' - очищает Ваше уведомление.");
            request = new AnswerInlineQuery(inlineQuery.id(), inlineCreate, inlineEdit, inlineDelete, inlineHelp).cacheTime(1);
            DoRequest(request);


        } else if (message != null) {
            long chatId = message.chat().id();
            if (message.text() != null && "/start".equals(message.text()) || "/start@Notifications1XklmrBots1_bot".equals(message.text())) {
                request = new SendMessage(chatId, "\uD83D\uDCD6Добавьте бот в нужный чат. Если бот уже добавлен, вызовите через '@' id бота и выберите нужное действие.");
                DoRequest(request);
            }
            if (message.text() != null && "/t".equals(message.text().substring(0, 2))) {
                notificationText = message.text().substring(2);
            }
            if (update.message().newChatMembers() != null && Arrays.stream(update.message().newChatMembers()).findFirst().get().username().equals("Notifications1XklmrBots1_bot")) {
                request = new SendMessage(chatId, "Привет\uD83D\uDC4B, " + message.from().firstName() + "! Бот активирован.\uD83E\uDD16 Введите команду /start");
                DoRequest(request);
            }


        } else if (callbackQuery != null) {
            Long chatId = callbackQuery.message().chat().id();
            switch (callbackQuery.data()) {
                case ("0"): {
                    if (notificationText.equals("Нет текста \uD83D\uDCE6")) {
                        request = new SendMessage(chatId, "Текст уведомления не был введен. Прочтите инструкцию выше\uD83D\uDC46");
                        DoRequest(request);
                    } else {
                        request = new SendMessage(chatId, "Текст уведомления сохранен!✅");
                        DoRequest(request);
                    }
                    break;
                }
                case ("1"): {
                    request = new SendMessage(chatId, "\uD83D\uDCD6Ваш текст уведомления: \n" + notificationText);
                    DoRequest(request);
                    break;
                }
                case ("2"): {
                    request = new SendMessage(chatId, "Новый текст уведомления сохранен!✅");
                    DoRequest(request);
                    break;
                }
            }
        }

    }

    public void DoRequest(BaseRequest request) {
        if (request != null) {
            bot.execute(request);
        }
    }
}