package bot.handlers.chatHandlers;

import bot.BotUtils;
import bot.entities.TGroup;
import bot.services.ChatService;
import bot.utils.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberMember;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class NewMembersHandlerTest {

    private final NewMembersHandler handler;
    private final ChatService chatRep;
    private final BotUtils botUtils;
    private final StringUtils stringUtils;
    private final AbsSender absSender;

    public NewMembersHandlerTest(@Mock ChatService chatRep, @Mock BotUtils botUtils, @Mock StringUtils stringUtils) {
        this.chatRep = chatRep;
        this.botUtils = botUtils;
        this.stringUtils = stringUtils;
        this.handler = new NewMembersHandler(chatRep, stringUtils, botUtils);
        this.absSender = Mockito.mock(AbsSender.class);
    }

    @BeforeEach
    void setupTest(){
        when(botUtils.getReadBlockMarkup()).thenCallRealMethod();
        when(stringUtils.fillParams(anyString(), any(Chat.class), any(User.class))).thenReturn(getWelcomeString());
    }

    @Test
    void whenNewUserJoinsChatWithoutNewUserRestrictedHeShouldBeWelcomedAndMessageDeleted() throws TelegramApiException {
        testHandler(false, null, 1, 0);
    }

    @Test
    void whenNewUserJoinsChatWithUserRestrictedHeShouldBeWelcomedAndRestricted() throws TelegramApiException {
       testHandler(true, botUtils.getReadBlockMarkup(), 1, 1);
    }

    private void testHandler(Boolean newUsersBlocked, ReplyKeyboardMarkup replyKeyboardMarkup, int numberOfSends, int numberOfRestricts) throws TelegramApiException {
        Update newChatMemberUpdate = getNewChatMemberUpdate();
        Chat tChat = newChatMemberUpdate.getChatMember().getChat();
        TGroup tGroup = getTestTGroup(tChat, newUsersBlocked);
        when(chatRep.get(anyLong())).thenReturn(Optional.of(tGroup));
        when(botUtils.canRestrictUsers(absSender, tChat, absSender.getMe())).thenReturn(true);
        this.handler.handle(absSender, newChatMemberUpdate);
        verify(botUtils, times(numberOfSends)).sendMessage(absSender, tChat, getWelcomeString(), null, replyKeyboardMarkup);
        verify(botUtils, times(numberOfRestricts)).restrictUserUntil(eq(absSender), eq(tChat), eq(newChatMemberUpdate.getChatMember().getFrom()), any(ZonedDateTime.class));
    }

    private TGroup getTestTGroup(Chat tChat, Boolean newUsersBlocked){
        TGroup tGroup = new TGroup(tChat);
        tGroup.setNew_users_blocked(newUsersBlocked);
        String welcomeString = getWelcomeString();
        tGroup.setWel_message(welcomeString);
        return tGroup;
    }

    private String getWelcomeString() {
        return  """
                Привет, iz. Перед тем как задать вопрос в обязательном порядке внимательно прочитайте
                закрепленное сообщение!  Если закрепленное сообщение не отображается,
                то можно найти его по ссылке: ⚠️https://test.com/djf ⚠️""";
    }

    private Update getNewChatMemberUpdate() {
        Chat chat = new Chat(1L, "supergroup");
        Update update = new Update();
        ChatMemberUpdated chatMemberUpdated = new ChatMemberUpdated();
        chatMemberUpdated.setChat(chat);
        ChatMemberMember chatMember = new ChatMemberMember();
        User user = new User(1L, "fio", false);
        chatMember.setUser(user);
        chatMemberUpdated.setNewChatMember(chatMember);
        chatMemberUpdated.setFrom(user);
        update.setChatMember(chatMemberUpdated);
        return update;
    }
}
