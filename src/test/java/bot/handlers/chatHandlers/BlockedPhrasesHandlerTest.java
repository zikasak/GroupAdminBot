package bot.handlers.chatHandlers;

import bot.BotUtils;
import bot.entities.TBlockedPhrase;
import bot.entities.TGroup;
import bot.services.BlockedPhraseService;
import bot.services.ChatService;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class BlockedPhrasesHandlerTest {

    private final BlockedPhrasesHandler handler;
    private final ChatService chatRep;
    private final BotUtils botUtils;
    private final BlockedPhraseService blockedPhraseService;
    private final AbsSender absSender;

    public BlockedPhrasesHandlerTest(@Mock ChatService chatRep, @Mock BotUtils botUtils, @Mock BlockedPhraseService blockedPhraseService) {
        this.chatRep = chatRep;
        this.botUtils = botUtils;
        this.blockedPhraseService = blockedPhraseService;
        this.handler = new BlockedPhrasesHandler(chatRep, botUtils, blockedPhraseService);
        this.absSender = Mockito.mock(AbsSender.class);
    }

    @BeforeEach
    void setUp(){
        when(chatRep.get(1L)).thenReturn(Optional.of(getGroupWithBlockedPhrase()));
        when(blockedPhraseService.getForGroup(getGroupWithBlockedPhrase())).thenReturn(Set.of(getBlockedPhraseObject()));
    }

    private TBlockedPhrase getBlockedPhraseObject() {
        TGroup groupWithBlockedPhrase = getGroupWithBlockedPhrase();
        TBlockedPhrase tBlockedPhrase = new TBlockedPhrase();
        tBlockedPhrase.setBlocked_phrase(getBlockedPhrase());
        tBlockedPhrase.setChat_id(groupWithBlockedPhrase.getChat_id());
        return tBlockedPhrase;
    }

    private String getBlockedPhrase() {
        return "blocked phrase";
    }

    @Test
    void messageWithBlockedPhraseShouldBeDeleted() throws TelegramApiException {
        Update update = getUpdateWithBlockedPhrase();
        handler.handle(absSender, update);
        verify(botUtils, times(1)).deleteMessage(absSender, update.getMessage(), absSender.getMe());
    }

    @Test
    void messageWithoutBlockedPhraseShouldBeStay() throws TelegramApiException {
        Update update = getUpdateWithOutBlockedPhrase();
        handler.handle(absSender, update);
        verify(botUtils, times(0)).deleteMessage(absSender, update.getMessage(), absSender.getMe());
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

    private Update getUpdateWithBlockedPhrase() {
        Update update = new Update();
        Message message = new Message();
        Chat supergroup = new Chat(1L, "supergroup");
        message.setChat(supergroup);
        message.setText(getBlockedPhrase());
        update.setMessage(message);
        return update;
    }
//
    private TGroup getGroupWithBlockedPhrase(){
        TGroup tGroup = new TGroup();
        tGroup.setChat_name("chat_name");
        tGroup.setChat_id(1L);
        return tGroup;
    }
}
