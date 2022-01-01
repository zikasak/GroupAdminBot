package bot.entities;

import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Set;

@Data
public class TMutedUser implements Serializable {

    private Long user_id;
    private Long chat_id;
    private ZonedDateTime mute_date;
    private Integer welcome_msg_id;
    private TGroup group;

    private Set<TTimeExceededMessage> time_messages;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TMutedUser that = (TMutedUser) o;
        return Objects.equals(user_id, that.user_id) && Objects.equals(chat_id, that.chat_id) && Objects.equals(mute_date, that.mute_date) && Objects.equals(welcome_msg_id, that.welcome_msg_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, chat_id, mute_date, welcome_msg_id);
    }
}
