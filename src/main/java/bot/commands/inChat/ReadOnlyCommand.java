package bot.commands.inChat;

import bot.BotUtils;
import bot.entities.TGroup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public abstract class ReadOnlyCommand extends InChatBotCommand {
    private final boolean startRO;

    public ReadOnlyCommand(String commandName, boolean startRO) {
        super(commandName, true, true, true);
        this.startRO = startRO;
    }

    protected abstract String getReadOnlyMessageText();

    @Override
    public void execute(AbsSender sender, TGroup tGroup, Message message, String[] strings) throws TelegramApiException {
        tGroup.setRead_only(this.startRO);
        botUtils.sendMessage(sender, message.getChat(), this.getReadOnlyMessageText());
        if (strings.length > 0){
            String text = String.join(" ", strings);
            botUtils.sendMessage(sender, message.getChat(), text);
        }
        this.service.save(tGroup);
    }
}
