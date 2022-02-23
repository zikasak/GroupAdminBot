package bot.commands.inChat;

import bot.entities.TGroup;
import bot.services.ChatService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class DeleteAdminCommand extends InChatBotCommand {

    private final ChatService chatService;

    public DeleteAdminCommand(ChatService chatService) {
        super("deleteAdmin", true, true, true);
        this.chatService = chatService;
    }

    @Override
    public void execute(AbsSender sender, TGroup chat, Message message, String... strings) throws TelegramApiException {
        Message replyToMessage = message.getReplyToMessage();
        if (replyToMessage == null) return;
        Long userId = replyToMessage.getFrom().getId();
        Long chatId = replyToMessage.getChatId();
        this.chatService.deleteAdministrator(chatId, userId);
    }
}
