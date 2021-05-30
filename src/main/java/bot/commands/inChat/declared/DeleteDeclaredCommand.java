package bot.commands.inChat.declared;

import bot.entities.TDeclaredCommand;
import bot.entities.TGroup;
import bot.reps.ChatRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DeleteDeclaredCommand extends DeclaredCommand {

    public DeleteDeclaredCommand() {
        super("delete", true, true, true);
    }

    @Override
    public void execute(AbsSender sender, TGroup tGroup, Message message, String[] strings) throws TelegramApiException {
        Chat chat = message.getChat();
        Set<TDeclaredCommand> command = tGroup.getCommand();
        String commandText = Arrays.stream(strings).skip(1).collect(Collectors.joining(" "));
        TDeclaredCommand tDeclaredCommand = new TDeclaredCommand(chat, strings[0], null);
        this.service.delete(tDeclaredCommand);
    }
}
