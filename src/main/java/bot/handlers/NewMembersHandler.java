package bot.handlers;

import bot.BotUtils;
import bot.entities.TGroup;
import bot.entities.TMutedUser;
import bot.entities.TMutedUserID;
import bot.reps.ChatRep;
import bot.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class NewMembersHandler implements IChannelHandler {

    @Autowired
    private ChatRep chatRep;
    @Autowired
    private StringUtils stringUtils;

    @Override
    public boolean checkHandle(AbsSender sender, Update update) {
        return true;
    }

    @Override
    @Transactional
    public void handle(AbsSender sender, Update update) {
        if (!checkHandle(sender, update)){
            return;
        }
        try {
            executeHandle(sender, update);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void executeHandle(AbsSender sender, Update update) throws TelegramApiException {
        Message updateMessage = update.getMessage();
        Chat telegramChat = updateMessage.getChat();
        List<User> newChatMembers = updateMessage.getNewChatMembers();
        Optional<TGroup> chatOpt = chatRep.findById(telegramChat.getId());
        if (chatOpt.isEmpty())
            return;
        TGroup chat = chatOpt.get();
        String welMessage = chat.getWel_message();
        if(welMessage == null || "".equals(welMessage.trim())) {
            deleteAfterSeen(sender, updateMessage);
            return;
        }
        for (User newMember : newChatMembers) {
            proceedNewMember(sender, updateMessage, newMember, chat);
        }
        chatRep.save(chat);
    }

    private void deleteAfterSeen(AbsSender sender, Message message){
        Runnable runnable = () -> {
            try {
                Thread.sleep(30000); //30 seconds
                BotUtils.deleteMessage(sender, message);
            } catch (TelegramApiException | InterruptedException e) {
                e.printStackTrace();
            }
        };
        runnable.run();
    }

    private void proceedNewMember(AbsSender sender, Message message, User newMember, TGroup chat) throws TelegramApiException {
        final boolean new_users_blocked = chat.isNew_users_blocked();
        final int time_to_mute = chat.getTime_to_mute();
        ReplyKeyboardMarkup readBlockMarkup = null;
        Chat tChat = message.getChat();
        boolean canRestrict = BotUtils.canRestrictUsers(sender, tChat, sender.getMe());
        if (new_users_blocked && canRestrict) {
            readBlockMarkup = BotUtils.getReadBlockMarkup();
        }

        String messageText = stringUtils.fillParams(message, newMember);
        BotUtils.sendMessage(sender, tChat, messageText, null, readBlockMarkup);
        if (!new_users_blocked || !canRestrict) {
            return;
        }
        ZonedDateTime restrictTo = ZonedDateTime.now(ZoneOffset.UTC).plusMinutes(time_to_mute);
        BotUtils.restrictUserUntil(sender, tChat, newMember, restrictTo);
        Set<TMutedUser> mutedUsers = chat.getMutedUsers();
        TMutedUser tMutedUser = new TMutedUser();
        TMutedUserID tMutedUserID = new TMutedUserID();
        tMutedUserID.setUser_id(newMember.getId());
        tMutedUserID.setChat_id(tChat.getId());
        tMutedUser.setId(tMutedUserID);
        tMutedUser.setMute_date(restrictTo);
        mutedUsers.remove(tMutedUser);
        mutedUsers.add(tMutedUser);

    }

}
