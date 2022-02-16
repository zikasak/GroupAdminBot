package bot.control.states;

import org.apache.shiro.session.Session;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public interface StateHandler {

    State handlingState();

    void handleStateEnter(AbsSender sender, Update update, Session session) throws TelegramApiException, IOException;

    State handleStateExecution(AbsSender sender, Update update, Session session) throws TelegramApiException, IOException;
}
