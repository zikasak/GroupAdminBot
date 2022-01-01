package bot.commands.inChat;

import bot.entities.TDeclaredCommand;
import bot.entities.TGroup;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import bot.services.CommandService;

import java.util.Optional;

@Component
public class GetCommandsCommand extends InChatBotCommand {

    private final CommandService commandService;

    public GetCommandsCommand(CommandService commandService) {
        super("getcommands", false, true, true);
        this.commandService = commandService;
    }

    @Override
    public void execute(AbsSender sender, TGroup tGroup, Message message, String[] strings) throws TelegramApiException {
        Chat chat = message.getChat();
        String commandString = "Команды для чата %s \n".formatted(chat.getTitle());
        Optional<String> commands = commandService.getForGroup(tGroup).stream()
                .map(TDeclaredCommand::getCommand)
                .reduce((fst, snd) -> fst + "\n" + snd);
        commandString += commands.orElse("");
        Chat userChat = new Chat();
        userChat.setId(message.getFrom().getId());
        botUtils.sendMessage(sender, userChat, commandString);
    }
}
