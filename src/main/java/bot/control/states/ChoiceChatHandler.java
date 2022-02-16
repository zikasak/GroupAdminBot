package bot.control.states;

import bot.Constants;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


@RequiredArgsConstructor
@Component
public class ChoiceChatHandler implements StateHandler {

    @Override
    public State handlingState() {
        return State.SELECT_CHAT;
    }

    @Override
    public void handleStateEnter(AbsSender sender, Update update, Session session) throws TelegramApiException, IOException {}

    @Override
    public State handleStateExecution(AbsSender sender, Update update, Session session) throws TelegramApiException, IOException {
        var chat = Long.valueOf(update.getCallbackQuery().getMessage().getText());
        Set<Long> chats = (Set<Long>) session.getAttribute(Constants.CHOSEN_CHATS);
        if (chats == null) chats = new HashSet<>();
        if (chats.contains(chat)) chats.remove(chat);
        else chats.add(chat);
        session.setAttribute(Constants.CHOSEN_CHATS, chats);
        return State.SELECT_CHAT;
    }
}
