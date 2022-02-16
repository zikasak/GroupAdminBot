package bot.control.states;

import bot.control.ProcessingResult;
import org.apache.shiro.session.Session;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface StateHandler {

    State getHandlingState();

    ProcessingResult onUpdateReceived(AbsSender sender, Update update, Session session) throws TelegramApiException;
}
