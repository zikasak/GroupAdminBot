package bot.commands.inChat.declared;

import bot.entities.TDeclaredCommand;
import bot.entities.TGroup;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Set;

@Component
public class AddDeclaredCommand extends DeclaredCommand{

    public AddDeclaredCommand(){
        super("add", true, true, true);
    }

    @Override
    @Transactional
    public void execute(AbsSender sender, TGroup tChat, Message message) {
        Chat chat = message.getChat();
        Set<TDeclaredCommand> command = tChat.getCommand();
        Pair<String, String> commandPair = this.parseCommand(message.getText());
        if (commandPair == null || !command.add(new TDeclaredCommand(chat, commandPair.getFirst(), commandPair.getSecond())))
            return;
        this.chatRep.save(tChat);
    }
}
