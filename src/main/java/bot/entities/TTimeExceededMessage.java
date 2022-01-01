package bot.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.Serializable;

@Data
@EqualsAndHashCode(exclude = {"user"})
public class TTimeExceededMessage implements Serializable {

    public TTimeExceededMessage() {}
    public TTimeExceededMessage(TMutedUser mutedUser, Message message) {
        this.chat_id = message.getChatId();
        this.user_id = mutedUser.getUser_id();
    }

    private long chat_id;
    private long user_id;
    private long message_id;
    private TMutedUser user;



}
