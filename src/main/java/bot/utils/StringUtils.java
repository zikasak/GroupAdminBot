package bot.utils;


import bot.utils.replacers.IParameterReplacer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

@Component
public class StringUtils {

    private final IParameterReplacer[] replacers;
    @Autowired
    public StringUtils(IParameterReplacer... replacers) {
        this.replacers = replacers;
    }

    public String fillParams(String text, Chat chat, User user ) {
        String result = text;
        for (IParameterReplacer replacer : replacers) {
            result = replacer.replaceWith(result, chat, user);
        }
        return result;
    }


}
