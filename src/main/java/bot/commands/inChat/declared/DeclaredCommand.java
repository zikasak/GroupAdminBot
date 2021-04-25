package bot.commands.inChat.declared;

import bot.commands.inChat.InChatBotCommand;
import org.springframework.data.util.Pair;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class DeclaredCommand extends InChatBotCommand {
    public DeclaredCommand(String commandName, boolean rightsNeeded, boolean deleteAfterUse, boolean chatShouldExists) {
        super(commandName, rightsNeeded, deleteAfterUse, chatShouldExists);
    }

    protected Pair<String, String> parseCommand(String command){
        Pattern regex = Pattern.compile("(/\\w+)( [\\s\\S\\w]+)?");
        Matcher matcher = regex.matcher(command);
        if (matcher.groupCount() < 2)
            return null;
        Pair<String, String> cmd = Pair.of(matcher.group(0), matcher.group(1));
        return cmd;
    }
}
