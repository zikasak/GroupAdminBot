package bot.Config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Value("${botToken}")
    @Getter private String botToken;
    @Value("${botName}")
    @Getter private String botName;

}
