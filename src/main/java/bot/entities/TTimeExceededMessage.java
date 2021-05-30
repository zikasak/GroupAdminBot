package bot.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="TTimeExceededMessage")
@Data
@IdClass(TTimeExceededMessage.class)
@EqualsAndHashCode(exclude = {"user"})
public class TTimeExceededMessage implements Serializable {

    public TTimeExceededMessage() {}
    public TTimeExceededMessage(TMutedUser mutedUser, Message message) {
        this.chat_id = message.getChatId();
        this.user_id = mutedUser.getId().getUser_id();
    }

    @Column(name = "chat_id", nullable = false, insertable = false)
    @Id
    private long chat_id;
    @Column(name = "user_id", nullable = false, insertable = false)
    @Id
    private long user_id;
    @Column(name = "message_id")
    @Id
    private long message_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumns({
            @PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "user_id"),
            @PrimaryKeyJoinColumn(name = "chat_id", referencedColumnName = "chat_id")
    })
    private TMutedUser user;



}
