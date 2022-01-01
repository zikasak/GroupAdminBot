package bot.commands.inChat.declared;

import bot.entities.TDeclaredCommand;
import bot.entities.TGroup;
import bot.services.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AddDeclaredCommand extends DeclaredCommand{

    private final ChatService chatService;

    public AddDeclaredCommand(ChatService chatService){
        super("add", true, true, true);
        this.chatService = chatService;
    }

    @Override
    public void execute(AbsSender sender, TGroup tGroup, Message message, String[] strings) throws TelegramApiException {
        Chat chat = message.getChat();
        String commandText = Arrays.stream(strings).skip(1).collect(Collectors.joining(" "));
        TDeclaredCommand tDeclaredCommand = new TDeclaredCommand(chat, strings[0], commandText);
        this.service.save(tDeclaredCommand);
        log.debug("Command " + tDeclaredCommand + " added successfully to chat " + chat);
    }
}
