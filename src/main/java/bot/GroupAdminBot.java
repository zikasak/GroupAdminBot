package bot;

import bot.Config.Configuration;
import bot.commands.DefaultCommand;
import bot.handlers.IChannelHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.ICommandRegistry;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class GroupAdminBot extends TelegramLongPollingCommandBot {

    private String botUsername;
    private String botToken;
    private IChannelHandler[] handlers;

    @Autowired
    public GroupAdminBot(Configuration configuration, DefaultCommand def, IBotCommand[] commands, IChannelHandler[] handlers) {
        this.botUsername = configuration.getBotName();
        this.botToken = configuration.getBotToken();
        this.handlers = handlers;
        initialize(def, commands);
    }

    @Override
    public String getBotUsername() {
        return this.botUsername;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        for (IChannelHandler handler : handlers) {
            try {
                if (handler.checkHandle(this, update))
                    handler.handle(this, update);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotToken() {
        return this.botToken;
    }

    private void initialize(DefaultCommand defaultCommand, IBotCommand[] registry){
        this.registerDefaultAction(defaultCommand);
        this.registerAll(registry);
    }
}
