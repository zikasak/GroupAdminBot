package bot.commands.inChat.phrases;

import bot.entities.TBlockedPhrase;
import bot.entities.TGroup;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class DeletePhraseCommand extends PhraseCommand {
    public DeletePhraseCommand() {
        super("delete", true, true, true);
    }

    @Override
    public void execute(AbsSender sender, TGroup tGroup, Message message, String[] strings) throws TelegramApiException {
        TBlockedPhrase tBlockedPhrase = new TBlockedPhrase();
        tBlockedPhrase.setBlocked_phrase(message.getText());
        tBlockedPhrase.setChat_id(tGroup.getChat_id());
        this.service.delete(tBlockedPhrase);
    }
}
