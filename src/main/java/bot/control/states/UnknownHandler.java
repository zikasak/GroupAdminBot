package bot.control.states;

import bot.control.KeyData;
import bot.utils.TelegramUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.ListUtils;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Component
public class UnknownHandler implements StateHandler{
    @Override
    public State handlingState() {
        return State.UNKNOWN;
    }

    @Override
    public void handleStateEnter(AbsSender sender, Update update, Session session) throws TelegramApiException, IOException {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("InitialKeyboard.json");
        ObjectMapper objectMapper = new ObjectMapper();
        List<InlineKeyboardButton> keys = Arrays.stream(objectMapper.readValue(resourceAsStream, KeyData[].class))
                .map(KeyData::button).toList();
        InlineKeyboardMarkup keyboard = InlineKeyboardMarkup.builder()
                .keyboard(ListUtils.partition(keys, 1))
                .build();
        SendMessage message = SendMessage.builder()
                .text("Выберите действие")
                .replyMarkup(keyboard)
                .chatId(String.valueOf(TelegramUtils.getChatId(update)))
                .build();
        sender.execute(message);
    }

    @Override
    public State handleStateExecution(AbsSender sender, Update update, Session session) throws TelegramApiException, IOException {
        if (!update.hasCallbackQuery()) return State.UNKNOWN;
        String data = update.getCallbackQuery().getData();
        return State.valueOf(data);
    }
}
