package bot.handlers.chatHandlers;

import bot.BotUtils;
import bot.entities.TGroup;
import bot.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Component
public class ReadOnlyHandler extends ChatHandler {

    private final ChatService chatService;
    private final BotUtils botUtils;

    @Autowired
    public ReadOnlyHandler(ChatService chatService, BotUtils botUtils) {
        super();
        this.chatService = chatService;
        this.botUtils = botUtils;
    }

    @Override
    protected boolean checkHandle(AbsSender sender, Update update) throws TelegramApiException {
        Message message = update.getMessage();
        if (message == null) return false;
        Optional<TGroup> byId = this.chatService.get(message.getChatId());
        if (byId.isEmpty()) return false;
        return byId.get().isRead_only() && !botUtils.isUserAdmin(sender, message);
    }

    @Override
    protected void handleMessage(AbsSender sender, Update update) throws TelegramApiException {
        botUtils.deleteMessage(sender, update.getMessage(), sender.getMe());
    }
}
