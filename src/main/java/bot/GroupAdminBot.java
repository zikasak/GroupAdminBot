package bot;
import bot.commands.DefaultCommand;
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
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.session.ChatIdConverter;
import org.telegram.telegrambots.session.DefaultChatIdConverter;
import org.telegram.telegrambots.session.DefaultChatSessionContext;

import java.util.Optional;

@Component
@Slf4j
public class GroupAdminBot extends TelegramLongPollingCommandBot {

    @Value("${botName}")
    private String botUsername;
    @Value("${botToken}")
    private String botToken;
    private final ChatHandler[] handlers;
    private DefaultSessionManager sessionManager;
    private ChatIdConverter chatIdConverter;

    @Autowired
    public GroupAdminBot(DefaultCommand def, IBotCommand[] commands, ChatHandler[] handlers, AllUpdatesBotOptions botOptions) {
        super(botOptions);
        this.handlers = handlers;
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
        if (update.hasMessage() && !filter(update.getMessage())) {
            proceedControlMessage(update);
            return;
        }
        for (ChatHandler handler : handlers) {
            try {
                handler.handle(this, update);
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage(), e);
            }
        }
    }

    private void proceedControlMessage(Update update) {
        Message message = update.getMessage();
        Optional<Session> chatSession = getSession(message);
        this.chatIdConverter.setSessionId(message.getChatId());
        chatSession = this.getSession(message);
        this.onUpdateReceived(update, chatSession);
    }

    private void onUpdateReceived(Update update, Optional<Session> chatSession) {

    }

    public Optional<Session> getSession(Message message) {
        try {
            return Optional.of(this.sessionManager.getSession(this.chatIdConverter));
        } catch (UnknownSessionException var4) {
            SessionContext botSession = new DefaultChatSessionContext(message.getChatId(), message.getFrom().getUserName());
            return Optional.of(this.sessionManager.start(botSession));
        }
    }
    @Override
    public boolean filter(Message message) {
        Long chatId = message.getChat().getId();
        Long userId = message.getFrom().getId();
        return !chatId.equals(userId);
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
