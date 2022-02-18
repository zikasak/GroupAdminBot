package bot.utils;

import bot.Constants;
import org.apache.commons.collections4.ListUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

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

}
