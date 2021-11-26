package by.ak.simple.telegram;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.stream.IntStream;

@Component
@Slf4j
public class TelegramPollingService {

    private TelegramBot bot;

    public void poll(String botToken) {
        if (StringUtils.isEmpty(botToken)) {
            return;
        }
        bot = new TelegramBot(botToken);
        bot.setUpdatesListener(updates -> {
            IntStream.rangeClosed(0, updates.size() - 1).forEach(i -> {
                var message = updates.get(i).message();
                String messageText = updates.get(i).message().text();
                String chatId = updates.get(i).message().chat().id().toString();
                if (messageText.equals("/start")) {
                    // todo consider to use a template
                    sendTextMessage(chatId, message.messageId(), "Hi there!");
                }
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    public void sendTextMessage(String chatId, int replayId, String text) {
        var request = new SendMessage(chatId, text)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(false)
                .disableNotification(false)
                .replyToMessageId(replayId);
        bot.execute(request, new Callback<SendMessage, SendResponse>() {
            @Override
            public void onResponse(SendMessage request, SendResponse response) {
                log.info("[SIMPLE] Sent message: {}", response.message().messageId());
            }

            @Override
            public void onFailure(SendMessage request, IOException e) {
                log.error("[SIMPLE] Failed to send message: {}", e.getMessage());
            }
        });
    }
}
