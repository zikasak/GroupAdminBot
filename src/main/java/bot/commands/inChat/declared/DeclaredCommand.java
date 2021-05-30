package bot.commands.inChat.declared;

import bot.commands.inChat.InChatBotCommand;
import org.springframework.data.util.Pair;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class DeclaredCommand extends InChatBotCommand {
    public DeclaredCommand(String commandName, boolean rightsNeeded, boolean deleteAfterUse, boolean chatShouldExists) {
        super(commandName, rightsNeeded, deleteAfterUse, chatShouldExists);
    }
}
