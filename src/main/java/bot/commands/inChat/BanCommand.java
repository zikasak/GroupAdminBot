package bot.commands.inChat;

import bot.BotUtils;
import bot.entities.TGroup;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class BanCommand extends InChatBotCommand {
    public BanCommand() {
        super("ban", true, true, true);
    }

    @Override
    public void execute(AbsSender sender, TGroup tGroup, Message message, String[] strings) throws TelegramApiException {
        botUtils.banUser(sender, message.getChat(), message.getReplyToMessage().getFrom());
    }
}
