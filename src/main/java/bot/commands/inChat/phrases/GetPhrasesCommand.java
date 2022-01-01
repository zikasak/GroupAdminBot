package bot.commands.inChat.phrases;

import bot.entities.TBlockedPhrase;
import bot.entities.TGroup;
import bot.services.BlockedPhraseService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GetPhrasesCommand extends PhraseCommand {
    private final BlockedPhraseService blockedPhraseService;

    public GetPhrasesCommand(BlockedPhraseService blockedPhraseService) {
        super("get", true, true, true, "s");
        this.blockedPhraseService = blockedPhraseService;
    }

    @Override
    public void execute(AbsSender sender, TGroup tGroup, Message message, String[] strings) throws TelegramApiException {
        Set<TBlockedPhrase> blockedPhrases = blockedPhraseService.getForGroup(tGroup);
        if (blockedPhrases.isEmpty()) {
            botUtils.sendMessage(sender, message.getChat(), "Нет запрещенных фраз");
            return;
        }
        String phrases = blockedPhrases.stream().map(TBlockedPhrase::getBlocked_phrase).collect(Collectors.joining("\n"));
        botUtils.sendMessage(sender, message.getChat(), phrases);
    }
}
