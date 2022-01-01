package bot.commands.inChat.declared;

import bot.commands.inChat.InChatBotCommand;

public abstract class DeclaredCommand extends InChatBotCommand {
    public DeclaredCommand(String commandName, boolean rightsNeeded, boolean deleteAfterUse, boolean chatShouldExists) {
        super(commandName, rightsNeeded, deleteAfterUse, chatShouldExists);
    }
}
