package bot.commands.inChat.phrases;

import bot.BotUtils;
import bot.entities.TBlockedPhrase;
import bot.entities.TGroup;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GetPhrasesCommand extends PhraseCommand {
    public GetPhrasesCommand() {
        super("get", true, true, true, "s");
    }

    @Override
    public void execute(AbsSender sender, TGroup tGroup, Message message, String[] strings) throws TelegramApiException {
        Set<TBlockedPhrase> blockedPhrases = tGroup.getBlockedPhrases();
        if (blockedPhrases.isEmpty()) {
            BotUtils.sendMessage(sender, message.getChat(), "Нет запрещенных фраз");
            return;
        }
        String phrases = blockedPhrases.stream().map((phrase) -> phrase.getId().getBlocked_phrase()).collect(Collectors.joining("\n"));
        BotUtils.sendMessage(sender, message.getChat(), phrases);
    }
}
