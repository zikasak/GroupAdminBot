package bot.commands.inChat;

import bot.entities.TGroup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MuteTimeCommand extends InChatBotCommand {
    public MuteTimeCommand(String commandName, boolean rightsNeeded, boolean deleteAfterUse, boolean chatCheckParam) {
        super("mutetime", true, true, true);
    }

    @Override
    public void execute(AbsSender sender, TGroup chat, Message message) throws TelegramApiException {
        String text = message.getText();
        Matcher pattern = Pattern.compile("^(\\d+)").matcher(text);
        if (!pattern.find())
            return;
        Integer duration = Integer.valueOf(pattern.group(0));
        chat.setTime_to_mute(duration);
        this.chatRep.save(chat);
    }
}
