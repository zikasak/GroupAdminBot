package bot.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Data
public class TBlockedPhraseID implements Serializable {

    @Column(name = "chat_id", nullable = false)
    private long chat_id;
    @Column(name = "blocked_phrase", nullable = false)
    private String blocked_phrase;


}
