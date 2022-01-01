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
    public String replaceWith(String text, Chat chat, User user) {
        Message pinnedMessage = chat.getPinnedMessage();
        if (pinnedMessage == null)
            return text.replace(getHandleParameter(), "");
        Integer pinnedId = pinnedMessage.getMessageId();
        long chatId = Long.parseLong(chat.getId().toString().substring(4));
        String replacement = "https://t.me/c/%d/%d".formatted(chatId, pinnedId);
        return text.replace(getHandleParameter(), replacement);
    }
}
