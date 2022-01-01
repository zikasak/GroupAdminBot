package bot;

import bot.entities.TGroup;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberAdministrator;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberMember;

public class TestUtils {

    public static ChatMemberMember getCommonChatMember() {
        ChatMemberMember chatMember = new ChatMemberMember();
        chatMember.setUser(new User(1L, "fio", false));
        return chatMember;
    }

    public static ChatMemberAdministrator getAnonymousAdminChatMember() {
        ChatMemberAdministrator chatMember = new ChatMemberAdministrator();
        chatMember.setIsAnonymous(true);
        chatMember.setUser(getAnonymousUser());
        return chatMember;
    }

    public static ChatMemberAdministrator getVisibleAdminChatMember() {
        ChatMemberAdministrator chatMember = new ChatMemberAdministrator();
        chatMember.setIsAnonymous(false);
        chatMember.setUser(getUser());
        return chatMember;
    }

    public static User getAnonymousUser() {
        return new User(1087968824L, "fio", false);
    }

    public static User getUser(){
        return new User(1L, "fio", false);
    }

    public static Update getUpdateFromUser() {
        Update update = new Update();
        Message message = new Message();
        message.setFrom(getUser());
        message.setChat(getChat());
        message.setText("fdsfgjkdfg");
        update.setMessage(message);
        return update;
    }

    public static Chat getChat() {
        return new Chat(1L, "supergroup");
    }

    public static TGroup getGroupWithReadOnly() {
        TGroup group = new TGroup(getChat());
        group.setRead_only(true);
        return group;
    }

    public static TGroup getGroupWithOutReadOnly() {
        return new TGroup(getChat());
    }

    public static Update getUpdateFromAdmin() {
        Update update = new Update();

        Message message = new Message();
        message.setFrom(getVisibleAdminChatMember().getUser());
        message.setChat(getChat());
        message.setText("fdsfgjkdfg");
        update.setMessage(message);
        return update;
    }
}
