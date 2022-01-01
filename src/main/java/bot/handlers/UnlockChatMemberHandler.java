package bot.handlers;

import bot.BotUtils;
import bot.entities.TMutedUser;
import bot.entities.TTimeExceededMessage;
import bot.mappers.MutedUserMapper;
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
public class UnlockChatMemberHandler extends ChannelHandler {

    private final MutedUserMapper mutedUserMapper;
    private final BotUtils botUtils;

    @Autowired
    public UnlockChatMemberHandler(MutedUserMapper mutedUserMapper, BotUtils botUtils) {
        this.mutedUserMapper = mutedUserMapper;
        this.botUtils = botUtils;
    }

    @Override
    protected boolean checkHandle(AbsSender sender, Update update) {
        return update.hasCallbackQuery();
    }

    @Override
    @Transactional
    protected void handleMessage(AbsSender sender, Update update) throws TelegramApiException {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Long chatId = callbackQuery.getMessage().getChatId();
        Long userId = callbackQuery.getFrom().getId();
        Optional<TMutedUser> byId = this.mutedUserMapper.findByUsedIdAndChatId(userId, chatId);
        if (byId.isEmpty()) return;
        TMutedUser tMutedUser = byId.get();
        ZonedDateTime mute_date = tMutedUser.getMute_date();
        if (ZonedDateTime.now().compareTo(mute_date) < 0) {
            Message message = botUtils.sendMessage(sender, update.getMessage().getChat(), "А лукавить нехорошо. Пожалуйста, прочитайте");
            tMutedUser.getTime_messages().add(new TTimeExceededMessage(tMutedUser, message));
            return;
        }
        for (TTimeExceededMessage time_message : tMutedUser.getTime_messages()) {
            botUtils.deleteMessage(sender, time_message);
        }
        this.mutedUserMapper.delete(tMutedUser.getUser_id(), tMutedUser.getChat_id());
        Message msg = new Message();
        msg.setChat(update.getMessage().getChat());
        msg.setMessageId(tMutedUser.getWelcome_msg_id());
        botUtils.deleteMessage(sender, msg);
        
    }
}
