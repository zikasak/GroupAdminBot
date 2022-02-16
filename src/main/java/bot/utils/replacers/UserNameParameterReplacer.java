package bot.utils.replacers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

@Component
public class UserNameParameterReplacer implements IParameterReplacer {
    @Override
    public String getHandleParameter() {
        return "${name}";
    }

    @Override
    public String replaceWith(String text, Chat chat, User user) {
        String replacement = String.format("<a href=\"tg://user?id=%d\">%s</a>", user.getId(), user.getFirstName());
        return text.replace(getHandleParameter(), replacement);
    }
}
