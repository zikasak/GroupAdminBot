package bot.commands.inChat.declared;

import bot.entities.TDeclaredCommand;
import bot.entities.TGroup;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Set;

@Component
public class UpdateDeclaredCommand extends DeclaredCommand {


    public UpdateDeclaredCommand() {
        super("update", true, true, true);
    }

    @Override
    public void execute(AbsSender sender, TGroup tGroup, Message message) {
        Chat chat = message.getChat();
        Set<TDeclaredCommand> command = tGroup.getCommand();
        Pair<String, String> commandPair = this.parseCommand(message.getText());
        if (commandPair == null) return;
        TDeclaredCommand tDeclaredCommand = new TDeclaredCommand(chat, commandPair.getFirst(), commandPair.getSecond());
        for (TDeclaredCommand cmd : command) {
            if (cmd.equals(tDeclaredCommand)) {
                cmd.setCommand_text(commandPair.getSecond());
            }
        }
        this.chatRep.save(tGroup);
    }
}
