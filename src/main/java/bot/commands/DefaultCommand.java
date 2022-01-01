package bot.commands;

import bot.BotUtils;
import bot.entities.TDeclaredCommand;
import bot.mappers.CommandMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.function.BiConsumer;

@Component
public class DefaultCommand implements BiConsumer<AbsSender, Message> {


    private final CommandMapper commandMapper;
    private final BotUtils botUtils;

    @Autowired
    public DefaultCommand(CommandMapper commandMapper, BotUtils botUtils) {
        this.commandMapper = commandMapper;
        this.botUtils = botUtils;
    }

    @Override
    public void accept(AbsSender absSender, Message message) {
        try {
            Long chatId = message.getChatId();
            String commandMessage = message.getText().substring(1);
            TDeclaredCommand command = commandMapper.getByIdChatIdAndIdCommand(chatId, commandMessage);
            if (command == null) return;
            Message replyToMessage = message.getReplyToMessage();
            Integer msgId = null;
            if (replyToMessage != null) {
                msgId = replyToMessage.getMessageId();
            }
            String replyText = command.getCommand_text();
            botUtils.sendMessage(absSender, message.getChat(), replyText, msgId);
            botUtils.deleteMessage(absSender, message, absSender.getMe());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
