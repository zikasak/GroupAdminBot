package bot.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Chat;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TDeclaredCommand implements Serializable {

    public TDeclaredCommand(Chat chat, String command, String command_text){
        this.command_text = command_text;
        this.chatId = chat.getId();
        this.command = command;
    }

    private long chatId;
    private String command;
    private String command_text;


}
