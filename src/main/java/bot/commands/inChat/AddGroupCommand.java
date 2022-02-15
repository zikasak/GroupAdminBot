package bot.commands.inChat;

import bot.entities.TGroup;
import bot.services.ChatService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;

@Component
public class AddGroupCommand extends InChatBotCommand {

    private final ChatService chatService;

    public AddGroupCommand(ChatService chatService) {
        super("addGroup", true, true, false);
        this.chatService = chatService;
    }

    @Override
    public void execute(AbsSender sender, TGroup chat, Message message, String[] strings) throws TelegramApiException {
        Long chatId = message.getChatId();
        TGroup tGroup = new TGroup();
        tGroup.setChat_id(chatId);
        tGroup.setChat_name(message.getChat().getTitle());
        this.service.save(tGroup);

        GetChatAdministrators getChatAdministrators = new GetChatAdministrators(String.valueOf(chatId));
        ArrayList<ChatMember> chatAdministrators = sender.execute(getChatAdministrators);
        chatAdministrators.stream()
                .map(ChatMember::getUser)
                .forEach((user) -> chatService.addAdministrator(tGroup, user));
    }
}
