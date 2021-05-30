package bot.commands.inChat;

import org.springframework.stereotype.Component;

@Component
public class StopReadOnlyCommand extends ReadOnlyCommand {
    public StopReadOnlyCommand() {
        super("stopRO", false);
    }

    @Override
    protected String getReadOnlyMessageText() {
        return "Режим 'Только чтение' отключен";
    }
}
