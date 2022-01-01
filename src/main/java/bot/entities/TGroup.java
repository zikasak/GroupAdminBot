package bot.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.Chat;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class TGroup implements Serializable {

    public TGroup(Chat chat){
        this.chat_id = chat.getId();
        this.chat_name = chat.getTitle();
        this.read_only = false;
        this.new_users_blocked = false;
    }

    private long chat_id;
    private String chat_name;
    private boolean read_only;
    private boolean new_users_blocked;
    private Integer time_to_mute = 5;
    private String wel_message;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TGroup tGroup = (TGroup) o;
        return chat_id == tGroup.chat_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chat_id);
    }
}
