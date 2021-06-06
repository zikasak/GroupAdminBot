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
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;

@Component
public class NewMembersHandler implements IChannelHandler {

    private final ChatRep chatRep;
    private final StringUtils stringUtils;
    private final BotUtils botUtils;

    @Autowired
    public NewMembersHandler(ChatRep chatRep, StringUtils stringUtils, BotUtils botUtils) {
        this.chatRep = chatRep;
        this.stringUtils = stringUtils;
        this.botUtils = botUtils;
    }

    @Override
    public boolean checkHandle(AbsSender sender, Update update) throws TelegramApiException {
        boolean userAdmin = botUtils.isUserAdmin(sender, update.getMessage().getChat(), sender.getMe());
        ChatMemberUpdated chatMember = update.getChatMember();
        return userAdmin && chatMember != null;
    }

    @Override
    @Transactional
    public void handle(AbsSender sender, Update update) throws TelegramApiException {
        executeHandle(sender, update);
    }

    private void executeHandle(AbsSender sender, Update update) throws TelegramApiException {
        ChatMemberUpdated chatMember = update.getChatMember();
        Chat telegramChat = chatMember.getChat();
        Optional<TGroup> chatOpt = chatRep.findById(telegramChat.getId());
        if (chatOpt.isEmpty())
            return;
        TGroup chat = chatOpt.get();
        String welMessage = chat.getWel_message();
        if(welMessage == null || "".equals(welMessage.trim())) {
            botUtils.deleteMessage(sender, update.getMessage());
            return;
        }
        proceedNewMember(sender, chatMember, chat);
        chatRep.save(chat);
    }

    private void proceedNewMember(AbsSender sender, ChatMemberUpdated chatMember, TGroup chat) throws TelegramApiException {
        final boolean new_users_blocked = chat.isNew_users_blocked();
        final int time_to_mute = chat.getTime_to_mute();
        User newMember = chatMember.getFrom();
        ReplyKeyboardMarkup readBlockMarkup = null;
        Chat tChat = chatMember.getChat();
        boolean canRestrict = botUtils.canRestrictUsers(sender, tChat, sender.getMe());
        if (new_users_blocked && canRestrict) {
            readBlockMarkup = botUtils.getReadBlockMarkup();
        }

        String messageText = stringUtils.fillParams(chat.getWel_message(), tChat, newMember);
        Message message = botUtils.sendMessage(sender, tChat, messageText, null, readBlockMarkup);
        if (!new_users_blocked || !canRestrict) {
            deleteAfterSeen(sender, message);
            return;
        }
        ZonedDateTime restrictTo = ZonedDateTime.now(ZoneOffset.UTC).plusMinutes(time_to_mute);
        botUtils.restrictUserUntil(sender, tChat, newMember, restrictTo);
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

    private void deleteAfterSeen(AbsSender sender, Message message){
        Runnable runnable = () -> {
            try {
                Thread.sleep(30000); //30 seconds
                botUtils.deleteMessage(sender, message);
            } catch (TelegramApiException | InterruptedException e) {
                e.printStackTrace();
            }
        };
        runnable.run();
    }

}
