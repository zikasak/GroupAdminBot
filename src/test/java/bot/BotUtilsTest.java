package bot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.ChatMember;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class BotUtilsTest {

    @Autowired
    private BotUtils botUtils;
    @Mock
    private AbsSender sender;
    private final Chat supergroup = new Chat(1L, "supergroup");

    @Test
    public void isUserAdminForAdminShouldBeTrue() throws TelegramApiException {
        ChatMember adminChatMember = getAdminChatMember();
        User fio = adminChatMember.getUser();
        GetChatMember getChatMember = new GetChatMember(Long.toString(supergroup.getId()), fio.getId());
        when(sender.execute(getChatMember)).thenReturn(adminChatMember);
        boolean userAdmin = botUtils.isUserAdmin(sender, supergroup, fio);
        assertTrue(userAdmin);
    }

    @Test
    public void isUserAdminForAnonymousShouldBeTrue() throws TelegramApiException {
        ChatMember anonymousAdminChatMember = getAnonymousAdminChatMember();
        User fio = anonymousAdminChatMember.getUser();
        GetChatMember getChatMember = new GetChatMember(Long.toString(supergroup.getId()), fio.getId());
        when(sender.execute(getChatMember)).thenReturn(anonymousAdminChatMember);
        boolean userAdmin = botUtils.isUserAdmin(sender, supergroup, fio);
        assertTrue(userAdmin);
    }

    @Test
    public void isUserAdminForCommonUserShouldBeFalse() throws TelegramApiException {
        ChatMember commonChatMember = getCommonChatMember();
        User fio = commonChatMember.getUser();
        GetChatMember getChatMember = new GetChatMember(Long.toString(supergroup.getId()), fio.getId());
        when(sender.execute(getChatMember)).thenReturn(commonChatMember);
        boolean userAdmin = botUtils.isUserAdmin(sender, supergroup, fio);
        assertFalse(userAdmin);
    }

    private ChatMember getCommonChatMember() {
        ChatMember chatMember = new ChatMember();
        chatMember.setStatus("user");
        chatMember.setUser(new User(1L, "fio", false));
        return chatMember;
    }

    private ChatMember getAnonymousAdminChatMember() {
        ChatMember chatMember = new ChatMember();
        chatMember.setIsAnonymous(true);
        chatMember.setStatus("administrator");
        chatMember.setUser(new User(1087968824L, "fio", false));
        return chatMember;
    }


    private ChatMember getAdminChatMember(){
        User fio = new User(1L, "fio", false);
        ChatMember chatMember = new ChatMember();
        chatMember.setStatus("administrator");
        chatMember.setUser(fio);
        return chatMember;
    }
}
