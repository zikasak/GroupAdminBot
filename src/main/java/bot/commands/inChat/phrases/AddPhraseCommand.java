package bot.commands.inChat.phrases;

import bot.entities.TBlockedPhrase;
import bot.entities.TBlockedPhraseID;
import bot.entities.TGroup;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Set;

@Component
public class AddPhraseCommand extends PhraseCommand {
    public AddPhraseCommand() {
        super("add", true, true, true);
    }

    @Override
    public void execute(AbsSender sender, TGroup tGroup, Message message, String[] strings) throws TelegramApiException {
        TBlockedPhrase tBlockedPhrase = new TBlockedPhrase();
        TBlockedPhraseID tBlockedPhraseID = new TBlockedPhraseID();
        tBlockedPhraseID.setBlocked_phrase(message.getText());
        tBlockedPhraseID.setChat_id(tGroup.getChat_id());
        tBlockedPhrase.setId(tBlockedPhraseID);
        this.service.save(tBlockedPhrase);
    }
}
