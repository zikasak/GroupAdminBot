package bot.utils;


import bot.utils.replacers.IParameterReplacer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class StringUtils {

    @Autowired
    private ParameterReplacerFactory parameterReplacerFactory;

    public String fillParams(Message message, User user ) {
        List<String> params = new LinkedList<>();
        String text = message.getText();
        Pattern compile = Pattern.compile("\\{\\$\\S+}");
        Matcher matcher = compile.matcher(text);
        for (int i = 0; i < matcher.groupCount(); i++) {
            String param = matcher.group(i);
            params.add(param);
        }
        for (var param: params) {
            IParameterReplacer iParameterReplacer = parameterReplacerFactory.getReplacer(param);
            text = iParameterReplacer.replaceWith(text, message, user);
        }
        return text;
    }


}
