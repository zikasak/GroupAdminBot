package bot.entities;

import lombok.Data;

import java.io.Serializable;

@Data
public class TGroupAdmin implements Serializable {

    private Long user_id;
    private Long chat_id;

}
