package bot.handlers;

import bot.BotUtils;
import bot.entities.TGroup;
import bot.reps.ChatRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Component
public class ReadOnlyHandler implements IChannelHandler {

    private final ChatRep chatRep;
    private final BotUtils botUtils;

    @Autowired
    public ReadOnlyHandler(ChatRep chatRep, BotUtils botUtils) {
        this.chatRep = chatRep;
        this.botUtils = botUtils;
    }

    @Override
    @Transactional
    public boolean checkHandle(AbsSender sender, Update update) throws TelegramApiException {
        Optional<TGroup> byId = this.chatRep.findById(update.getMessage().getChatId());
        if (byId.isEmpty()) return false;
        return byId.get().isRead_only() && !botUtils.isUserAdmin(sender, update.getMessage().getChat(), update.getMessage().getFrom());
    }

    @Override
    public void handle(AbsSender sender, Update update) throws TelegramApiException {
        botUtils.deleteMessage(sender, update.getMessage(), sender.getMe());
    }
}
