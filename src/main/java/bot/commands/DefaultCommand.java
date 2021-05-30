package bot.commands;

import bot.BotUtils;
import bot.entities.TDeclaredCommand;
import bot.reps.CommandRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.function.BiConsumer;

@Component
public class DefaultCommand implements BiConsumer<AbsSender, Message> {

    @Autowired
    private CommandRep commandRep;

    @Override
    public void accept(AbsSender absSender, Message message) {
        try {
            Long chatId = message.getChatId();
            String commandMessage = message.getText();
            TDeclaredCommand command = commandRep.getByIdChatIdAndIdCommand(chatId, commandMessage);
            if (command == null) return;
            Message replyToMessage = message.getReplyToMessage();
            Integer msgId = null;
            if (replyToMessage != null) {
                msgId = replyToMessage.getMessageId();
            }
            String replyText = command.getCommand_text();
            BotUtils.sendMessage(absSender, message.getChat(), replyText, msgId);
            BotUtils.deleteMessage(absSender, message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
