package bot.utils;

import bot.Constants;
import bot.control.states.State;
import org.apache.commons.collections4.ListUtils;
import org.apache.shiro.session.Session;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

public class TelegramUtils {

    public static List<List<InlineKeyboardButton>> getKeyboardWithBackButton(List<InlineKeyboardButton> buttons, int columnCount) {
        List<List<InlineKeyboardButton>> partition = new ArrayList<>(ListUtils.partition(buttons, columnCount));
        List<InlineKeyboardButton> backButtonRow = List.of(InlineKeyboardButton.builder().text("Назад").callbackData(Constants.RETURN_BACK).build());
        partition.add(backButtonRow);
        return partition;
    }

    public static Long getChatId(Update update) {
        if (update.hasCallbackQuery())
            return update.getCallbackQuery().getFrom().getId();
        return update.getMessage().getChatId();
    }

    public static String getUserName(Update update) {
        if (update.hasCallbackQuery())
            return update.getCallbackQuery().getFrom().getUserName();
        return update.getMessage().getFrom().getUserName();
    }

    public static void clearSession(Session session) {
        session.getAttributeKeys().forEach(session::removeAttribute);
        Stack<State> states = new Stack<>();
        states.push(State.UNKNOWN);
        session.setAttribute(Constants.CHOSEN_CHATS, new HashSet<Long>());
        session.setAttribute(Constants.STATE_STACK, states);
    }

}
