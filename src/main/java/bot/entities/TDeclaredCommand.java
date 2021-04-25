package bot.entities;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "TDeclaredCommand")
public class TDeclaredCommand implements Serializable {

    public TDeclaredCommand(){}

    public TDeclaredCommand(Chat chat, String command, String command_text){
        this.command_text = command_text;
        this.id = new TDeclaredCommandID(chat, command);

    }

    @EmbeddedId
    private TDeclaredCommandID id;
    @Column(name = "command_text")
    private String command_text;

    @ManyToOne(fetch = FetchType.LAZY)
    private TGroup group;

    public long  getChat_id() {
        return this.id.getChat_id();
    }

    public String getCommand() {
        return this.id.getCommand();
    }

    public TDeclaredCommandID getId() {
        return id;
    }

    public void setId(TDeclaredCommandID id) {
        this.id = id;
    }

    public String getCommand_text() {
        return command_text;
    }

    public void setCommand_text(String command_text) {
        this.command_text = command_text;
    }

    public TGroup getGroup() {
        return group;
    }

    public void setGroup(TGroup group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TDeclaredCommand that = (TDeclaredCommand) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
