package bot.commands.inChat;

import bot.BotUtils;
import bot.entities.TGroup;
import bot.reps.ChatRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

public abstract class InChatBotCommand implements IBotCommand {

    @Autowired
    protected ChatRep chatRep;

    protected boolean rightsNeeded;
    protected boolean deleteAfterUse;
    protected String commandName;
    private final boolean chatCheckParam;

    public InChatBotCommand(String commandName, boolean rightsNeeded, boolean deleteAfterUse, boolean chatCheckParam) {
        this.commandName = commandName;
        this.rightsNeeded = rightsNeeded;
        this.deleteAfterUse = deleteAfterUse;
        this.chatCheckParam = chatCheckParam;
    }

    @Override
    public String getCommandIdentifier() {
        return this.commandName;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    @Transactional
    public void processMessage(AbsSender absSender, Message message, String[] strings) {
        try {
            String text = message.getText();
            String replace = text.replace("/" + this.commandName + " ", "");
            message.setText(replace);
            Optional<TGroup> chat = this.chatRep.findById(message.getChatId());
            if (!this.rightsNeeded && chat.isPresent() == this.chatCheckParam)
                this.execute(absSender, chat.orElse(null), message);
            if (this.deleteAfterUse){
                BotUtils.deleteMessage(absSender, message);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public abstract void execute(AbsSender sender, TGroup chat, Message message) throws TelegramApiException;

    public boolean isRightsNeeded() {
        return rightsNeeded;
    }

    public boolean isDeleteAfterUse() {
        return deleteAfterUse;
    }
}
