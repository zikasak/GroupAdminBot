package bot.utils.replacers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

@Component
public class PinnedMessageParameterReplacer implements IParameterReplacer {
    @Override
    public String getHandleParameter() {
        return "${pinned_message}";
    }

    @Override
    public String replaceWith(String text, Message message, User user) {
        Chat chat = message.getChat();
        Integer pinnedId = chat.getPinnedMessage().getMessageId();
        String userName = chat.getUserName();
        String replacement = "https://t.me/%s/%d".formatted(userName, pinnedId);
        return text.replace(getHandleParameter(), replacement);
    }
}
