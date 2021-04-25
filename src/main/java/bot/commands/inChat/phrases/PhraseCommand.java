package bot.commands.inChat.phrases;

import bot.commands.inChat.InChatBotCommand;

public abstract class PhraseCommand extends InChatBotCommand {
    public PhraseCommand(String commandName, boolean rightsNeeded, boolean deleteAfterUse, boolean chatCheckParam) {
        super(commandName + "Phrase", rightsNeeded, deleteAfterUse, chatCheckParam);
    }

    public PhraseCommand(String commandName, boolean rightsNeeded, boolean deleteAfterUse, boolean chatCheckParam, String postfix) {
        super(commandName + "Phrase" + postfix, rightsNeeded, deleteAfterUse, chatCheckParam);
    }

}
