package bot;

import bot.entities.TGroup;
import bot.entities.TTimeExceededMessage;
import org.checkerframework.checker.units.qual.K;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.groupadministration.KickChatMember;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;

public class BotUtils {

    public static boolean isUserAdmin(AbsSender sender, Chat chat, User user) throws TelegramApiException {
        ChatMember chatMember = getChatMember(sender, chat, user);
        return chatMember.getIsAnonymous() || chatMember.getStatus().equals("administrator") || chatMember.getStatus().equals("creator");
    }

    public static Message sendMessage(AbsSender sender, Chat chat, String text) throws TelegramApiException {
        return sendMessage(sender, chat, text, null, null);
    }

    public static Message sendMessage(AbsSender sender, Chat chat, String text, Integer replyId) throws TelegramApiException {
        return sendMessage(sender, chat, text, null, null);
    }

    public static Message sendMessage(AbsSender sender, Chat chat, String text, Integer replyId, ReplyKeyboardMarkup markup) throws TelegramApiException {
        Long chatId = chat.getId();
        SendMessage msg = new SendMessage();
        msg.setText(text);
        msg.setChatId(Long.toString(chatId));
        msg.setReplyMarkup(markup);
        if (replyId != null) {
            msg.setReplyToMessageId(replyId);
        }
        return sender.execute(msg);
    }

    public static void deleteMessage(AbsSender sender, TTimeExceededMessage message) throws TelegramApiException {
        Chat chat = new Chat();
        chat.setId(message.getChat_id());
        Message msg = new Message();
        msg.setChat(chat);
        msg.setMessageId((int) message.getMessage_id());
        msg.setFrom(sender.getMe());
        deleteMessage(sender, msg);
    }

    public static void deleteMessage(AbsSender sender, Message message) throws TelegramApiException {
        if (!canDeleteMessages(sender, message.getChat(), message.getFrom()))
            return;
        DeleteMessage deleteMessage = new DeleteMessage(message.getChatId().toString(), message.getMessageId());
        sender.execute(deleteMessage);

    }

    public static void banUser(AbsSender sender, Chat chat, User user) throws TelegramApiException {
        KickChatMember kickChatMember = new KickChatMember();
        kickChatMember.setChatId(String.valueOf(chat.getId()));
        kickChatMember.setUserId(user.getId());
        sender.execute(kickChatMember);
    }

    public static void restrictUserUntil(AbsSender sender, Chat chat, User user, ZonedDateTime restrictTo) throws TelegramApiException {
        ChatPermissions permissions = ChatPermissions.builder().canSendMessages(false).canSendOtherMessages(false).canSendPolls(false).build();
        restrictUser(sender, chat, user, restrictTo, permissions);
    }

    public static void restrictUser(AbsSender sender, Chat chat, User user, ZonedDateTime restrictTo, ChatPermissions permissions) throws TelegramApiException {
        if (!canRestrictUsers(sender, chat, user))
            return;
        RestrictChatMember restrictChatMember = new RestrictChatMember();
        restrictChatMember.setChatId(String.valueOf(chat.getId()));
        restrictChatMember.setUserId(user.getId());
        restrictChatMember.setUntilDateDateTime(restrictTo);
        restrictChatMember.setPermissions(permissions);
        sender.execute(restrictChatMember);
    }

    public static boolean canRestrictUsers(AbsSender sender, Chat chat, User user) throws TelegramApiException {
        ChatMember chatMember = getChatMember(sender, chat, user);
        return chatMember.getCanRestrictMembers();
    }

    public static boolean canDeleteMessages(AbsSender sender, Chat chat, User user) throws TelegramApiException {
        ChatMember chatMember = getChatMember(sender, chat, user);
        return chatMember.getCanDeleteMessages();
    }

    private static ChatMember getChatMember(AbsSender sender, Chat chat, User user) throws TelegramApiException {
        GetChatMember getChatMember = new GetChatMember(Long.toString(chat.getId()), user.getId());
        ChatMember chatMember = sender.execute(getChatMember);
        return chatMember;
    }

    public static ReplyKeyboardMarkup getReadBlockMarkup(){
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        KeyboardRow row = new KeyboardRow();
        row.add("Я прочитал");
        markup.setKeyboard(Collections.singletonList(row));
        return markup;
    }
}
