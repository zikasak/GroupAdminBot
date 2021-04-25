package bot.entities;

import org.telegram.telegrambots.meta.api.objects.Chat;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class TDeclaredCommandID implements Serializable {
    @Column(name = "chat_id")
    private long chat_id;
    @Column(name = "command")
    private String command;

    public TDeclaredCommandID(){}

    public TDeclaredCommandID(Chat chat, String command) {
        this.chat_id = chat.getId();
        this.command = command;
    }

    public long  getChat_id() {
        return chat_id;
    }

    public void setChat_id(long chat_id) {
        this.chat_id = chat_id;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
