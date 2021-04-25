package bot.handlers;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.function.Function;

public interface IChannelHandler{

    boolean checkHandle(AbsSender sender, Update update) throws TelegramApiException;
    void handle(AbsSender sender, Update update) throws TelegramApiException;
}
