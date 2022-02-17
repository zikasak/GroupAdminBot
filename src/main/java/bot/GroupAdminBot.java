package bot;
import bot.commands.DefaultCommand;
import bot.control.StateController;
import bot.handlers.chatHandlers.ChatHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.session.ChatIdConverter;
import org.telegram.telegrambots.session.DefaultChatIdConverter;
import org.telegram.telegrambots.session.DefaultChatSessionContext;


@Component
@Slf4j
public class GroupAdminBot extends TelegramLongPollingCommandBot {

    @Value("${botName}")
    private String botUsername;
    @Value("${botToken}")
    private String botToken;
    private final ChatHandler[] handlers;
    private final StateController stateController;
    private final DefaultSessionManager sessionManager;
    private final ChatIdConverter chatIdConverter;

    @Autowired
    public GroupAdminBot(DefaultCommand def, IBotCommand[] commands, ChatHandler[] handlers, AllUpdatesBotOptions botOptions,
                         StateController stateController) {
        super(botOptions);
        this.handlers = handlers;
        this.stateController = stateController;
        this.sessionManager = new DefaultSessionManager();
        this.chatIdConverter = new DefaultChatIdConverter();
        AbstractSessionDAO sessionDAO = (AbstractSessionDAO)this.sessionManager.getSessionDAO();
        sessionDAO.setSessionIdGenerator(chatIdConverter);
        initialize(def, commands);
    }

    @Override
    public String getBotUsername() {
        return this.botUsername;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if ((update.hasMessage() || update.hasCallbackQuery()) && !filter(update)) {
            proceedControlMessage(update);
            return;
        }
        for (ChatHandler handler : handlers) {
            try {
                handler.handle(this, update);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private void proceedControlMessage(Update update) {
        Message message = update.getMessage();
        Session chatSession;
        this.chatIdConverter.setSessionId(getChatId(update));
        chatSession = this.getSession(message);
        this.stateController.onUpdateReceived(this, update, chatSession);
    }

    private Long getChatId(Update update) {
        if (update.hasMessage()) return update.getMessage().getChatId();
        if (update.hasCallbackQuery()) return update.getCallbackQuery().getFrom().getId();
        return null;
    }

    public Session getSession(Message message) {
        try {
            return this.sessionManager.getSession(this.chatIdConverter);
        } catch (UnknownSessionException var4) {
            SessionContext botSession = new DefaultChatSessionContext(message.getChatId(), message.getFrom().getUserName());
            return this.sessionManager.start(botSession);
        }
    }

    public boolean filter(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            Long chatId = message.getChat().getId();
            Long userId = message.getFrom().getId();
            return !chatId.equals(userId);
        }
        if (update.hasCallbackQuery()){
            CallbackQuery callbackQuery = update.getCallbackQuery();
            Long userId = callbackQuery.getFrom().getId();
            Long chatId = callbackQuery.getMessage().getChatId();
            return !chatId.equals(userId);
        }
        return false;
    }

    @Override
    public String getBotToken() {
        return this.botToken;
    }

    private void initialize(DefaultCommand defaultCommand, IBotCommand... registry){
        this.registerDefaultAction(defaultCommand);
        this.registerAll(registry);
    }
}
