package bot.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.NotFound;
import org.telegram.telegrambots.meta.api.objects.Chat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "TGroup")
@Data
@EqualsAndHashCode(exclude = {"admins", "command", "mutedUsers", "blockedPhrases"})
public class TGroup implements Serializable {

    public TGroup(){}

    public TGroup(Chat chat){
        this.chat_id = chat.getId();
        this.chat_name = chat.getTitle();
        this.read_only = false;
        this.new_users_blocked = false;
    }

    @Id
    @Column(name = "chat_id")
    private long chat_id;
    @Column(name = "chat_name")
    private String chat_name;
    @Column(name = "read_only")
    private boolean read_only;
    @Column(name = "new_users_blocked")
    private boolean new_users_blocked;
    @Column(name = "time_to_mute")
    private Integer time_to_mute = 5;
    @Column(name = "wel_message")
    private String wel_message;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<TGroupAdmin> admins;
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<TDeclaredCommand> command;
    @OneToMany(mappedBy = "id.chat_id", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<TMutedUser> mutedUsers;
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "chat_id")
    private Set<TBlockedPhrase> blockedPhrases;


}
