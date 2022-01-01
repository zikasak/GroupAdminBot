package bot.entities;

import lombok.Data;

import java.io.Serializable;


@Data
public class TBlockedPhrase implements Serializable {

    private long chat_id;
    private String blocked_phrase;
}
