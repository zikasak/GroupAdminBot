package bot.commands.inChat.declared;

import bot.entities.TDeclaredCommand;
import bot.entities.TGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import bot.services.CommandService;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DeleteDeclaredCommand extends DeclaredCommand {

    public DeleteDeclaredCommand() {
        super("delete", true, true, true);
    }

    @Override
    public void execute(AbsSender sender, TGroup tGroup, Message message, String[] strings) throws TelegramApiException {
        Chat chat = message.getChat();
        TDeclaredCommand tDeclaredCommand = new TDeclaredCommand(chat, strings[0], null);
        this.service.delete(tDeclaredCommand);
        log.debug("Command " + tDeclaredCommand + " deleted successfully from chat " + chat);
    }
}
