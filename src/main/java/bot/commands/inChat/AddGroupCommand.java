package bot.commands.inChat;

import bot.entities.TGroup;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class AddGroupCommand extends InChatBotCommand {
    public AddGroupCommand() {
        super("addGroup", true, true, false);
    }

    @Override
    public void execute(AbsSender sender, TGroup chat, Message message, String[] strings) throws TelegramApiException {
        Long chatId = message.getChatId();
        TGroup tGroup = new TGroup();
        tGroup.setChat_id(chatId);
        tGroup.setChat_name(message.getChat().getTitle());
        this.service.save(tGroup);
    }
}
