package bot.commands.inChat;

import bot.entities.TGroup;
import bot.entities.TGroupAdmin;
import bot.services.ChatService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Set;

@Component
public class RefreshAdminsCommand extends InChatBotCommand {

    private final ChatService chatService;

    public RefreshAdminsCommand(ChatService chatService) {
        super("refreshAdmins", true, true, true);
        this.chatService = chatService;
    }

    @Override
    public void execute(AbsSender sender, TGroup chat, Message message, String... strings) throws TelegramApiException {
        Set<TGroupAdmin> administratorList = this.chatService.getAdministratorList(chat);
        GetChatAdministrators getChatAdministrators = new GetChatAdministrators(String.valueOf(chat.getChat_id()));
        ArrayList<ChatMember> chatAdministrators = sender.execute(getChatAdministrators);
        administratorList.stream().filter((gr) -> !gr.isAdditional())
                .forEach(administratorList::remove);
        chatAdministrators.forEach((admin) -> {
            TGroupAdmin tGroupAdmin = new TGroupAdmin();
            tGroupAdmin.setUser_id(admin.getUser().getId());
            tGroupAdmin.setChat_id(chat.getChat_id());
            administratorList.add(tGroupAdmin);
        });
        this.chatService.setAdministratorList(administratorList);
    }
}
