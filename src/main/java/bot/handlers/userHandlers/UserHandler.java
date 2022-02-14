package bot.handlers.userHandlers;


import org.apache.shiro.session.Session;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public abstract class UserHandler {

    protected abstract boolean checkHandle(AbsSender sender, Update update, Session session) throws TelegramApiException;
    public void handle(AbsSender sender, Update update, Session session) throws TelegramApiException {
        if (!checkHandle(sender, update, session)) return;
        this.handleMessage(sender, update, session);
    }

    protected abstract void handleMessage(AbsSender sender, Update update, Session session) throws TelegramApiException;

}
