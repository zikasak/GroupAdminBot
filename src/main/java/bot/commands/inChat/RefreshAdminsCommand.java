package bot.commands.inChat;

import bot.entities.TGroup;
import bot.entities.TGroupAdmin;
import bot.services.ChatService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RefreshAdminsCommand extends InChatBotCommand {

    private final ChatService chatService;

    public RefreshAdminsCommand(ChatService chatService) {
        super("refreshAdmins", true, true, true);
        this.chatService = chatService;
    }

    @Override
    public void execute(AbsSender sender, TGroup chat, Message message, String... strings) throws TelegramApiException {
        GetChatAdministrators getChatAdministrators = new GetChatAdministrators(String.valueOf(chat.getChat_id()));
        ArrayList<ChatMember> chatAdministrators = sender.execute(getChatAdministrators);
        List<Long> userIds = chatAdministrators.stream().map(ChatMember::getUser)
                .map(User::getId).toList();
        this.chatService.setAdministratorList(chat.getChat_id(), userIds);
    }
}
