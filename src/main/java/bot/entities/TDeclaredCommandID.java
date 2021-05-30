package bot.entities;

import org.telegram.telegrambots.meta.api.objects.Chat;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class TDeclaredCommandID implements Serializable {
    @Column(name = "chat_id")
    private long chatId;
    @Column(name = "command")
    private String command;

    public TDeclaredCommandID(){}

    public TDeclaredCommandID(Chat chat, String command) {
        this.chatId = chat.getId();
        this.command = command;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
