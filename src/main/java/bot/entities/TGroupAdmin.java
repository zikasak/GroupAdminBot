package bot.entities;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class TGroupAdmin implements Serializable {

    private Long user_id;
    private Long chat_id;

//    @JoinColumn(name = "chat_id", updatable = false, insertable = false)
    private Set<TGroup> groups;


}
