package bot.config;

import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    public DefaultBotOptions botOptions(){
        DefaultBotOptions defaultBotOptions = new DefaultBotOptions();
//        defaultBotOptions.getAllowedUpdates().add("chat_member");
        return defaultBotOptions;
    }


}
