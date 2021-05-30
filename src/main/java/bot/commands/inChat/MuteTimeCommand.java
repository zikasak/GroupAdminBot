package bot.commands.inChat;

import bot.entities.TGroup;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MuteTimeCommand extends InChatBotCommand {
    public MuteTimeCommand() {
        super("mutetime", true, true, true);
    }

    @Override
    public void execute(AbsSender sender, TGroup tGroup, Message message, String[] strings) throws TelegramApiException {
        String text = message.getText();
        Matcher pattern = Pattern.compile("^(\\d+)").matcher(text);
        if (!pattern.find())
            return;
        Integer duration = Integer.valueOf(pattern.group(0));
        tGroup.setTime_to_mute(duration);
        this.service.save(tGroup);
    }
}
