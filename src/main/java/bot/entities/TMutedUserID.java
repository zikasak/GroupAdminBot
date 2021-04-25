package bot.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TMutedUserID implements Serializable {

    @Column(name = "chat_id")
    private long chat_id;
    @Column(name = "user_id")
    private long user_id;

    public long getChat_id() {
        return chat_id;
    }

    public void setChat_id(long chat_id) {
        this.chat_id = chat_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TMutedUserID that = (TMutedUserID) o;
        return user_id == that.user_id && Objects.equals(chat_id, that.chat_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chat_id, user_id);
    }
}
