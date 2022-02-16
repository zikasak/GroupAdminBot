package bot.control.states;

import bot.control.ProcessingResult;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class UnknownHandler implements StateHandler{
    @Override
    public State getHandlingState() {
        return State.UNKNOWN;
    }

    @Override
    public ProcessingResult onUpdateReceived(AbsSender sender, Update update, Session session) throws TelegramApiException {
        return null;
    }
}
