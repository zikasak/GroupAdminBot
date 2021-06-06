package bot.handlers;

import bot.BotUtils;
import bot.entities.TBlockedPhrase;
import bot.entities.TGroup;
import bot.reps.ChatRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;

@Component
public class BlockedPhrasesHandler implements IChannelHandler {

    private final ChatRep chatRep;
    private final BotUtils botUtils;

    @Autowired
    public BlockedPhrasesHandler(ChatRep chatRep, BotUtils botUtils) {
        this.chatRep = chatRep;
        this.botUtils = botUtils;
    }

    @Override
    public boolean checkHandle(AbsSender sender, Update update) throws TelegramApiException {
        return !botUtils.isUserAdmin(sender, update.getMessage().getChat(), update.getMessage().getFrom());
    }

    @Override
    public void handle(AbsSender sender, Update update) throws TelegramApiException {
        Optional<TGroup> byId = this.chatRep.findById(update.getMessage().getChatId());
        if (byId.isEmpty()) return;
        TGroup tGroup = byId.get();
        Set<TBlockedPhrase> blockedPhrases = tGroup.getBlockedPhrases();
        String text = " " + update.getMessage().getText().toLowerCase(Locale.ROOT) + " ";
        for (TBlockedPhrase blockedPhrase : blockedPhrases) {
            String search_phrase = " " + blockedPhrase.getId().getBlocked_phrase() + " ";
            if (text.contains(search_phrase)){
                botUtils.deleteMessage(sender, update.getMessage());
                return;
            }
        }
    }
}
