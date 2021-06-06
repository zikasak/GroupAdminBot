package bot.utils;


import bot.utils.replacers.IParameterReplacer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class StringUtils {


    private ParameterReplacerFactory parameterReplacerFactory;
    @Autowired
    public StringUtils(ParameterReplacerFactory parameterReplacerFactory) {
        this.parameterReplacerFactory = parameterReplacerFactory;
    }

    public String fillParams(String text, Chat chat, User user ) {
        List<String> params = new LinkedList<>();
        Pattern compile = Pattern.compile("\\{\\$\\S+}");
        Matcher matcher = compile.matcher(text);
        for (int i = 0; i < matcher.groupCount(); i++) {
            String param = matcher.group(i);
            params.add(param);
        }
        for (var param: params) {
            IParameterReplacer iParameterReplacer = parameterReplacerFactory.getReplacer(param);
            text = iParameterReplacer.replaceWith(text, chat, user);
        }
        return text;
    }


}
