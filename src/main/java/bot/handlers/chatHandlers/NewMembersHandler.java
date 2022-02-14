package bot.handlers.chatHandlers;

import bot.BotUtils;
import bot.entities.TGroup;
import bot.services.ChatService;
import bot.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberMember;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;

@Component
@Slf4j
public class NewMembersHandler extends ChatHandler {

    private final ChatService chatService;
    private final StringUtils stringUtils;
    private final BotUtils botUtils;

    @Autowired
    public NewMembersHandler(ChatService chatService, StringUtils stringUtils, BotUtils botUtils) {
        this.chatService = chatService;
        this.stringUtils = stringUtils;
        this.botUtils = botUtils;
    }

    @Override
    protected boolean checkHandle(AbsSender sender, Update update) {
        ChatMemberUpdated updateChatMember = update.getChatMember();
        if (updateChatMember == null) return false;
        return updateChatMember.getNewChatMember().getStatus().equals("member");
    }

    @Override
    @Transactional
    protected void handleMessage(AbsSender sender, Update update) throws TelegramApiException {
        log.debug("Handling new member");
        ChatMemberUpdated updatedChatMember = update.getChatMember();
        ChatMemberMember chatMember = (ChatMemberMember) updatedChatMember.getNewChatMember();
        Chat telegramChat = botUtils.getChat(sender, updatedChatMember.getChat().getId());
        Optional<TGroup> chatOpt = chatService.get(telegramChat.getId());
        if (chatOpt.isEmpty()) {
            log.debug("Chat doesn't found in database");
            return;
        }
        TGroup chat = chatOpt.get();
        String welMessage = chat.getWel_message();
        if(welMessage == null || "".equals(welMessage.trim())) {
            log.debug("New members are not blocked");
            botUtils.deleteMessage(sender, update.getMessage());
            return;
        }
        proceedNewMember(sender, chatMember.getUser(), telegramChat, chat);
    }

    private void proceedNewMember(AbsSender sender, User newMember, Chat tChat, TGroup chat) throws TelegramApiException {
        final boolean new_users_blocked = chat.isNew_users_blocked();
        final int time_to_mute = chat.getTime_to_mute();
        ReplyKeyboardMarkup readBlockMarkup = null;
        boolean canRestrict = botUtils.canRestrictUsers(sender, tChat, sender.getMe());
        boolean needRestrict = new_users_blocked && canRestrict;
        if (needRestrict) {
            readBlockMarkup = botUtils.getReadBlockMarkup();
        }
        String messageText = stringUtils.fillParams(chat.getWel_message(), tChat, newMember);
        Message message = botUtils.sendMessage(sender, tChat, messageText, null, readBlockMarkup);
        if (!needRestrict) {
            log.debug("No need to restrict user");
            deleteAfterSeen(sender, message);
            return;
        }
        ZonedDateTime restrictTo = ZonedDateTime.now(ZoneOffset.UTC).plusMinutes(time_to_mute);
        botUtils.restrictUserUntil(sender, tChat, newMember, restrictTo);
    }

    private void deleteAfterSeen(AbsSender sender, Message message){
        Runnable runnable = () -> {
            try {
                Thread.sleep(30000); //30 seconds
                botUtils.deleteMessage(sender, message);
                log.debug("Welcome message deleted successfully");
            } catch (TelegramApiException | InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        };
        runnable.run();
    }

}
