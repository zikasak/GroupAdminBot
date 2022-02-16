package bot.utils.replacers;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

public interface IParameterReplacer {

    String getHandleParameter();
    String replaceWith(String text, Chat chat, User user);

}
