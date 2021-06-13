package bot.handlers;

import bot.BotUtils;
import bot.entities.TBlockedPhrase;
import bot.entities.TBlockedPhraseID;
import bot.entities.TGroup;
import bot.reps.ChatRep;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashSet;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class BlockedPhrasesHandlerTest {

    private final BlockedPhrasesHandler handler;
    private final ChatRep chatRep;
    private final BotUtils botUtils;
    private final AbsSender absSender;

    public BlockedPhrasesHandlerTest(@Mock ChatRep chatRep, @Mock BotUtils botUtils) {
        this.chatRep = chatRep;
        this.botUtils = botUtils;
        this.handler = new BlockedPhrasesHandler(chatRep, botUtils);
        this.absSender = Mockito.mock(AbsSender.class);
    }

    @Test
    void messageWithBlockedPhraseShouldBeDeleted() throws TelegramApiException {
        String phrase = "blocked phrase";
        when(chatRep.findById(1L)).thenReturn(getGroupWithBlockedPhrase(phrase));
        Update update = getUpdateWithBlockedPhrase(phrase);
        handler.handle(absSender, update);
        verify(botUtils, times(1)).deleteMessage(absSender, update.getMessage());
    }

    @Test
    void messageWithoutBlockedPhraseShouldBeStay() throws TelegramApiException {
        String phrase = "blocked phrase";
        when(chatRep.findById(1L)).thenReturn(getGroupWithBlockedPhrase(phrase));
        Update update = getUpdateWithOutBlockedPhrase();
        handler.handle(absSender, update);
        verify(botUtils, times(0)).deleteMessage(absSender, update.getMessage());
    }

    private Update getUpdateWithOutBlockedPhrase() {
        Update update = new Update();
        Message message = new Message();
        Chat supergroup = new Chat(1L, "supergroup");
        message.setChat(supergroup);
        message.setText("yes");
        update.setMessage(message);
        return update;
    }

    private Update getUpdateWithBlockedPhrase(String phrase) {
        Update update = new Update();
        Message message = new Message();
        Chat supergroup = new Chat(1L, "supergroup");
        message.setChat(supergroup);
        message.setText(phrase);
        update.setMessage(message);
        return update;
    }

    private Optional<TGroup> getGroupWithBlockedPhrase(String phrase){
        TGroup tGroup = new TGroup();
        tGroup.setChat_name("chat_name");
        tGroup.setChat_id(1L);
        HashSet<TBlockedPhrase> hashSet = new HashSet<>();
        TBlockedPhrase tBlockedPhrase = new TBlockedPhrase();
        TBlockedPhraseID id = new TBlockedPhraseID();
        id.setChat_id(1L);
        id.setBlocked_phrase(phrase);
        tBlockedPhrase.setId(id);
        hashSet.add(tBlockedPhrase);
        tGroup.setBlockedPhrases(hashSet);
        return Optional.of(tGroup);
    }
}
