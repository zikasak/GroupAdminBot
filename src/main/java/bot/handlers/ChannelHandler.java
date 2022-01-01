package bot.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public abstract class ChannelHandler{

    protected abstract boolean checkHandle(AbsSender sender, Update update) throws TelegramApiException;
    public void handle(AbsSender sender, Update update) throws TelegramApiException {
        if (!checkHandle(sender, update)) return;
        this.handleMessage(sender, update);
    }

    protected abstract void handleMessage(AbsSender sender, Update update) throws TelegramApiException;
}
