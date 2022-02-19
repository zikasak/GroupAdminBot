package bot.control.states;

import bot.Constants;
import bot.entities.TGroup;
import bot.services.ChatService;
import bot.utils.TelegramUtils;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ChangeReadOnlyHandler implements StateHandler{

    private final ChatService chatService;
    private final static Set<String> possibleValues = Set.of("TURN_OFF", "TURN_ON");
    @Override
    public State handlingState() {
        return State.CHANGING_READ_ONLY;
    }

    @Override
    public void handleStateEnter(AbsSender sender, Update update, Session session) throws TelegramApiException, IOException {
        List<InlineKeyboardButton> buttons = List.of(InlineKeyboardButton.builder().text("Включить").callbackData("TURN_ON").build(),
                InlineKeyboardButton.builder().text("Выключить").callbackData("TURN_OFF").build());

        List<List<InlineKeyboardButton>> keyboard = TelegramUtils.getKeyboardWithBackButton(buttons, 1);
        InlineKeyboardMarkup keyboardMarkup = InlineKeyboardMarkup.builder().keyboard(keyboard).build();
        SendMessage message = SendMessage.builder().text("Выберите действие")
                .chatId(String.valueOf(update.getCallbackQuery().getFrom().getId()))
                .replyMarkup(keyboardMarkup)
                .build();
        sender.execute(message);
    }

    @Override
    public State handleStateExecution(AbsSender sender, Update update, Session session) throws TelegramApiException, IOException {
        Set<Long> chats = (Set<Long>) session.getAttribute(Constants.CHOSEN_CHATS);
        if (chats.isEmpty()) return State.UNKNOWN;
        String data = update.getCallbackQuery().getData();
        if (!possibleValues.contains(data)) return State.UNKNOWN;
        boolean turnOnReadOnly = "TURN_ON".equals(data);
        for (Long chat : chats) {
            Optional<TGroup> tGroup = chatService.get(chat);
            if (tGroup.isPresent()) {
                TGroup group = tGroup.get();
                group.setRead_only(turnOnReadOnly);
                chatService.save(group);
                sendReadOnlyMessage(sender, group.getChat_id(), turnOnReadOnly);
            }
        }
        sendReadOnlyMessage(sender, update.getCallbackQuery().getFrom().getId(), turnOnReadOnly);
        return State.CHANGING_READ_ONLY;
    }

    private void sendReadOnlyMessage(AbsSender sender, long chatId, boolean turnOnReadOnly) throws TelegramApiException {
        SendMessage message = SendMessage.builder()
                .text("Режим 'Только чтение' " + (turnOnReadOnly ? "включен" : "выключен"))
                .chatId(String.valueOf(chatId))
                .build();
        sender.execute(message);
    }
}
