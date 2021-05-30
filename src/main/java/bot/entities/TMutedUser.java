package bot.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "TMutedUser")
@Data public class TMutedUser implements Serializable {

    @EmbeddedId
    private TMutedUserID id;
    @Column(name = "mute_date")
    private ZonedDateTime mute_date;
    @Column(name = "welcome_msg_id", nullable = false)
    private Integer welcome_msg_id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TGroup group;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<TTimeExceededMessage> time_messages;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TMutedUser that = (TMutedUser) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
