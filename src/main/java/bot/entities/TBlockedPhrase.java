package bot.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "TBlockedPhrase")
@EqualsAndHashCode(exclude = {"group"})
@Data
public class TBlockedPhrase implements Serializable {
    @EmbeddedId
    private TBlockedPhraseID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TGroup group;

}
