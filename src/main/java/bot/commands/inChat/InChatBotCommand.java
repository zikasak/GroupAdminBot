package bot.commands.inChat;

import bot.BotUtils;
import bot.entities.TGroup;
import bot.services.InChatBotCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Slf4j
public abstract class InChatBotCommand implements IBotCommand {

    @Autowired
    protected InChatBotCommandService service;
    @Autowired
    protected BotUtils botUtils;

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
    public void processMessage(AbsSender absSender, Message message, String[] strings) {
        try {
            this.service.processMessage(absSender, message, strings, this);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }

    public abstract void execute(AbsSender sender, TGroup chat, Message message, String... strings) throws TelegramApiException;

    public boolean isRightsNeeded() {
        return rightsNeeded;
    }

    public boolean isDeleteAfterUse() {
        return deleteAfterUse;
    }

    public boolean isChatCheckParam() {
        return chatCheckParam;
    }
}
