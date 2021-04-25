package bot.handlers;

import bot.BotUtils;
import bot.entities.TMutedUser;
import bot.entities.TMutedUserID;
import bot.entities.TTimeExceededMessage;
import bot.reps.MutedUsersRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.ZonedDateTime;
import java.util.Optional;

@Component
public class UnlockChatMemberHandler implements IChannelHandler {
    @Autowired
    private MutedUsersRep mutedUsersRep;

    @Override
    public boolean checkHandle(AbsSender sender, Update update) {
        return update.hasCallbackQuery();
    }

    @Override
    @Transactional
    public void handle(AbsSender sender, Update update) throws TelegramApiException {
        if (!checkHandle(sender, update)){
            return;
        }
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer userId = callbackQuery.getFrom().getId();
        TMutedUserID id = new TMutedUserID();
        id.setChat_id(chatId);
        id.setUser_id(userId);
        Optional<TMutedUser> byId = this.mutedUsersRep.findById(id);
        if (byId.isEmpty()) return;
        TMutedUser tMutedUser = byId.get();
        ZonedDateTime mute_date = tMutedUser.getMute_date();
        if (ZonedDateTime.now().compareTo(mute_date) < 0) {
            Message message = BotUtils.sendMessage(sender, update.getMessage().getChat(), "А лукавить нехорошо. Пожалуйста, прочитайте");
            tMutedUser.getTime_messages().add(new TTimeExceededMessage(tMutedUser, message));
            return;
        }
        for (TTimeExceededMessage time_message : tMutedUser.getTime_messages()) {
            BotUtils.deleteMessage(sender, time_message);
        }
        this.mutedUsersRep.delete(tMutedUser);
        Message msg = new Message();
        msg.setChat(update.getMessage().getChat());
        msg.setMessageId(tMutedUser.getWelcome_msg_id());
        BotUtils.deleteMessage(sender, msg);
        
    }
}
