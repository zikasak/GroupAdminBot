package bot.control;

import org.apache.shiro.session.Session;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;


public interface StateController {

    void onUpdateReceived(AbsSender sender, Update update, Session chatSession);

}
