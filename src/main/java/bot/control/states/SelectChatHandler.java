package bot.control.states;

import bot.entities.TGroup;
import bot.services.ChatService;
import lombok.RequiredArgsConstructor;
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
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SelectChatHandler implements StateHandler {

    private final ChatService chatService;

    private static InlineKeyboardButton createKeyboardButton(TGroup group) {
        return InlineKeyboardButton.builder()
                .callbackData(String.valueOf(group.getChat_id()))
                .text(group.getChat_name())
                .build();
    }

    @Override
    public State handlingState() {
        return State.SELECTING_CHAT;
    }

    @Override
    public void handleStateEnter(AbsSender sender, Update update, Session session) throws TelegramApiException {
        Long userId = update.getCallbackQuery().getFrom().getId();
        Set<TGroup> groupList = chatService.getGroupList(userId);
        List<InlineKeyboardButton> buttons = groupList.stream()
                .map(SelectChatHandler::createKeyboardButton).toList();
        List<List<InlineKeyboardButton>> keyboard = ListUtils.partition(buttons, 3);
        InlineKeyboardMarkup keyboardMarkup = InlineKeyboardMarkup.builder().keyboard(keyboard).build();
        SendMessage message = SendMessage.builder()
                .chatId(String.valueOf(userId))
                .replyMarkup(keyboardMarkup)
                .build();
        sender.execute(message);
    }

    @Override
    public State handleStateExecution(AbsSender sender, Update update, Session session) throws TelegramApiException, IOException {
        return null;
    }

}
