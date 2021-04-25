package bot.utils.replacers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

@Component
public class UserNameParameterReplacer implements IParameterReplacer {
    @Override
    public String getHandleParameter() {
        return "{$name}";
    }

    @Override
    public String replaceWith(String text, Message message, User user) {
        String replacement = String.format("<a href=\"tg://user?id={%d}\">{%s}</a>", user.getId(), user.getFirstName());
        return text.replace(getHandleParameter(), replacement);
    }
}
