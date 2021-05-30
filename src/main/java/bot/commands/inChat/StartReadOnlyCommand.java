package bot.commands.inChat;

import org.springframework.stereotype.Component;

@Component
public class StartReadOnlyCommand extends ReadOnlyCommand {
    public StartReadOnlyCommand() {
        super("startRO", true);
    }

    @Override
    protected String getReadOnlyMessageText() {
        return "Активирован режим 'Только чтение'.";
    }
}
