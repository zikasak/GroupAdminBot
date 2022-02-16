package bot.handlers.chatHandlers;

import bot.BotUtils;
import bot.TestUtils;
import bot.services.ChatService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class ReadOnlyHandlerTest {

    private final ReadOnlyHandler handler;
    private final BotUtils botUtils;
    private final ChatService chatService;
    private final AbsSender absSender;

    public ReadOnlyHandlerTest(@Mock ChatService chatService, @Mock BotUtils botUtils) {
        this.chatService = chatService;
        this.botUtils = botUtils;
        this.handler = new ReadOnlyHandler(chatService, botUtils);
        absSender = Mockito.mock(AbsSender.class);
    }

    @Test
    void messageFromUserShouldBeDeletedWhenReadOnlyActive() throws TelegramApiException {
        Update update = TestUtils.getUpdateFromUser();
        when(chatService.get(1L)).thenReturn(Optional.of(TestUtils.getGroupWithReadOnly()));
        boolean b = handler.checkHandle(absSender, update);
        assertTrue(b);
        handler.handle(absSender, update);
        verify(botUtils, times(1)).deleteMessage(absSender, update.getMessage(), absSender.getMe());
    }

    @Test
    void messageFromUserShouldNotBeDeletedWhenReadOnlyDisable() throws TelegramApiException {
        Update update = TestUtils.getUpdateFromUser();
        when(chatService.get(1L)).thenReturn(Optional.of(TestUtils.getGroupWithOutReadOnly()));
        boolean b = handler.checkHandle(absSender, update);
        assertFalse(b);
    }

    @Test
    void messageFromAdminShouldBeNeverDeleted() throws TelegramApiException {
        Update update = TestUtils.getUpdateFromAdmin();
    }

}
