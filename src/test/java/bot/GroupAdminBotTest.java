package bot;

import bot.GroupAdminBot;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class GroupAdminBotTest {

    @Autowired
    GroupAdminBot bot;

    @Test
    public void testAddingGroup(){
        Update update = new Update();
        Message msg = new Message();
        User user = new User();
        user.setId(1087968824L);
        msg.setFrom(user);
        msg.setMessageId(223);
        msg.setText("/addGroup");
        Chat chat = new Chat();
        chat.setId(-1001155230821L);
        msg.setChat(chat);
        update.setMessage(msg);
        bot.onUpdatesReceived(List.of(update));
    }

    @Test
    public void testReadOnlyOnCommand(){
        Update update = new Update();
        Message msg = new Message();
        User user = new User();
        user.setId(1087968824L);
        msg.setFrom(user);
        msg.setMessageId(223);
        msg.setText("/startRO");
        Chat chat = new Chat();
        chat.setId(-1001155230821L);
        msg.setChat(chat);
        update.setMessage(msg);
        bot.onUpdatesReceived(List.of(update));
    }
}
