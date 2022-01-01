package bot;

import bot.entities.TTimeExceededMessage;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.groupadministration.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberAdministrator;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.ZonedDateTime;
import java.util.Collections;

@Component
public class BotUtils {

    public Chat getChat(AbsSender absSender, long chatId) throws TelegramApiException {
        GetChat build = GetChat.builder()
                .chatId(String.valueOf(chatId))
                .build();
        return absSender.execute(build);
    }

    public boolean isUserAdmin(AbsSender sender, Message message) throws TelegramApiException {
        Chat senderChat = message.getSenderChat();
        if (senderChat != null) return true;
        Chat chat = message.getChat();
        User user = message.getFrom();
        return isUserAdmin(sender, chat, user);
    }

    public boolean isUserAdmin(AbsSender sender, Chat chat, User user) throws TelegramApiException {
        ChatMember chatMember = getChatMember(sender, chat, user);
        return chatMember.getStatus().equals("administrator") || chatMember.getStatus().equals("creator");
    }

    public Message sendMessage(AbsSender sender, Chat chat, String text) throws TelegramApiException {
        return sendMessage(sender, chat, text, null, null);
    }

    public Message sendMessage(AbsSender sender, Chat chat, String text, Integer replyId) throws TelegramApiException {
        return sendMessage(sender, chat, text, null, null);
    }

    public Message sendMessage(AbsSender sender, Chat chat, String text, Integer replyId, ReplyKeyboardMarkup markup) throws TelegramApiException {
        Long chatId = chat.getId();
        SendMessage msg = new SendMessage();
        msg.setText(text);
        msg.setParseMode(ParseMode.HTML);
        msg.setChatId(Long.toString(chatId));
        msg.setReplyMarkup(markup);
        msg.setDisableWebPagePreview(true);
        if (replyId != null) {
            msg.setReplyToMessageId(replyId);
        }
        return sender.execute(msg);
    }

    public void deleteMessage(AbsSender sender, TTimeExceededMessage message) throws TelegramApiException {
        Chat chat = new Chat();
        chat.setId(message.getChat_id());
        Message msg = new Message();
        msg.setChat(chat);
        msg.setMessageId((int) message.getMessage_id());
        msg.setFrom(sender.getMe());
        deleteMessage(sender, msg);
    }

    public void deleteMessage(AbsSender sender, Message message) throws TelegramApiException {
        deleteMessage(sender, message, message.getFrom());
    }

    public void deleteMessage(AbsSender sender, Message message, User user) throws TelegramApiException {
        if (!canDeleteMessages(sender, message.getChat(), user))
            return;
        DeleteMessage deleteMessage = new DeleteMessage(message.getChatId().toString(), message.getMessageId());
        sender.execute(deleteMessage);
    }

    public void banUser(AbsSender sender, Chat chat, User user) throws TelegramApiException {
        BanChatMember banChatMember = new BanChatMember();
        banChatMember.setChatId(String.valueOf(chat.getId()));
        banChatMember.setUserId(user.getId());
        sender.execute(banChatMember);
    }

    public void restrictUserUntil(AbsSender sender, Chat chat, User user, ZonedDateTime restrictTo) throws TelegramApiException {
        ChatPermissions permissions = ChatPermissions.builder().canSendMessages(false).canSendOtherMessages(false).canSendPolls(false).build();
        restrictUser(sender, chat, user, restrictTo, permissions);
    }

    public void restrictUser(AbsSender sender, Chat chat, User user, ZonedDateTime restrictTo, ChatPermissions permissions) throws TelegramApiException {
        if (!canRestrictUsers(sender, chat, user))
            return;
        RestrictChatMember restrictChatMember = new RestrictChatMember();
        restrictChatMember.setChatId(String.valueOf(chat.getId()));
        restrictChatMember.setUserId(user.getId());
        restrictChatMember.setUntilDateDateTime(restrictTo);
        restrictChatMember.setPermissions(permissions);
        sender.execute(restrictChatMember);
    }

    public boolean canRestrictUsers(AbsSender sender, Chat chat, User user) throws TelegramApiException {
        ChatMember chatMember = getChatMember(sender, chat, user);
        if (!(chatMember instanceof ChatMemberAdministrator))
            return false;
        ChatMemberAdministrator administrator = (ChatMemberAdministrator) chatMember;
        return administrator.getCanRestrictMembers();
    }

    public boolean canDeleteMessages(AbsSender sender, Chat chat, User user) throws TelegramApiException {
        ChatMember chatMember = getChatMember(sender, chat, user);
        if (!(chatMember instanceof ChatMemberAdministrator))
            return false;
        ChatMemberAdministrator administrator = (ChatMemberAdministrator) chatMember;
        return administrator.getCanDeleteMessages() != null && administrator.getCanDeleteMessages();
    }

    private ChatMember getChatMember(AbsSender sender, Chat chat, User user) throws TelegramApiException {
        GetChatMember getChatMember = new GetChatMember(Long.toString(chat.getId()), user.getId());
        ChatMember chatMember = sender.execute(getChatMember);
        return chatMember;
    }

    public ReplyKeyboardMarkup getReadBlockMarkup(){
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        KeyboardRow row = new KeyboardRow();
        row.add("Я прочитал");
        markup.setKeyboard(Collections.singletonList(row));
        return markup;
    }
}
