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

import java.util.Set;

@Component
public class DeleteDeclaredCommand extends DeclaredCommand {

    @Autowired
    private ChatRep chatRep;

    public DeleteDeclaredCommand() {
        super("delete", true, true, true);
    }

    @Override
    @Transactional
    public void execute(AbsSender sender, TGroup tGroup, Message message) {
        Chat chat = message.getChat();
        Set<TDeclaredCommand> command = tGroup.getCommand();
        Pair<String, String> commandPair = this.parseCommand(message.getText());
        TDeclaredCommand tDeclaredCommand = new TDeclaredCommand(chat, commandPair.getFirst(), null);
        command.remove(tDeclaredCommand);
        chatRep.save(tGroup);
    }
}
