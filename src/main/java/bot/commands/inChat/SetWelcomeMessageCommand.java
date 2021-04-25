package bot.commands.inChat;

import bot.entities.TGroup;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SetWelcomeMessageCommand extends InChatBotCommand {
    public SetWelcomeMessageCommand() {
        super("setwelcomemessage", true, true, true);
    }

    @Override
    public void execute(AbsSender sender, TGroup tGroup, Message message) throws TelegramApiException {
        Pattern pattern = Pattern.compile("^(?<param>b )?(?<text>[\\s\\S\\w]+)");
        Matcher matcher = pattern.matcher(message.getText());
        if (matcher.groupCount() == 0) return;
        String param = matcher.group("param");
        boolean new_users_blocked = param != null && !param.isBlank();
        String welMessage = matcher.group("text");
        tGroup.setWel_message(welMessage);
        tGroup.setNew_users_blocked(new_users_blocked);
        this.chatRep.save(tGroup);
    }
}
