package bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;

import java.util.List;

@Component
public class AllUpdatesBotOptions extends DefaultBotOptions {

    public AllUpdatesBotOptions() {
        super();
        this.setAllowedUpdates(List.of("message", "chat_member", "callback_query"));
    }
}
