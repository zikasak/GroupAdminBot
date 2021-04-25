package bot.utils.replacers;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

public interface IParameterReplacer {

    String getHandleParameter();
    String replaceWith(String text, Message message, User user);

}
