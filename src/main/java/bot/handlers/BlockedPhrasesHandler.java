package bot.handlers;

import bot.BotUtils;
import bot.entities.TBlockedPhrase;
import bot.entities.TGroup;
import bot.mappers.ChatMapper;
import bot.services.BlockedPhraseService;
import bot.services.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;

@Component
@AllArgsConstructor
public class BlockedPhrasesHandler extends ChannelHandler {

    private final ChatService chatRep;
    private final BotUtils botUtils;
    private final BlockedPhraseService blockedPhraseService;

    @Override
    protected boolean checkHandle(AbsSender sender, Update update) throws TelegramApiException {
        return update.getMessage() != null
                && !botUtils.isUserAdmin(sender, update.getMessage())
                && update.getMessage().getText() != null;
    }

    @Override
    protected void handleMessage(AbsSender sender, Update update) throws TelegramApiException {
        Optional<TGroup> byId = this.chatRep.get(update.getMessage().getChatId());
        if (byId.isEmpty()) return;
        TGroup tGroup = byId.get();
        Set<TBlockedPhrase> blockedPhrases = blockedPhraseService.getForGroup(tGroup);
        String text = " " + update.getMessage().getText().toLowerCase(Locale.ROOT) + " ";
        for (TBlockedPhrase blockedPhrase : blockedPhrases) {
            String search_phrase = " " + blockedPhrase.getBlocked_phrase() + " ";
            if (text.contains(search_phrase)){
                botUtils.deleteMessage(sender, update.getMessage(), sender.getMe());
                return;
            }
        }
    }
}
