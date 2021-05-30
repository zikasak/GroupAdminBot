package bot.commands.inChat;

import bot.BotUtils;
import bot.entities.TDeclaredCommand;
import bot.entities.TGroup;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Component
public class GetCommandsCommand extends InChatBotCommand {

    public GetCommandsCommand() {
        super("getcommands", false, true, true);
    }

    @Override
    public void execute(AbsSender sender, TGroup tGroup, Message message, String[] strings) throws TelegramApiException {
        Chat chat = message.getChat();
        String commandString = "Команды для чата %s \n".formatted(chat.getTitle());
        Optional<String> commands = tGroup.getCommand().stream()
                .map(TDeclaredCommand::getCommand)
                .reduce((fst, snd) -> fst + "\n" + snd);
        commandString += commands.orElse("");
        Chat userChat = new Chat();
        userChat.setId(message.getFrom().getId());
        BotUtils.sendMessage(sender, userChat, commandString);
    }
}
