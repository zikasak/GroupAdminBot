package bot.utils;

import bot.control.states.State;
import org.apache.commons.collections4.ListUtils;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class TelegramUtils {

    public static List<List<InlineKeyboardButton>> getKeyboardWithBackButton(List<InlineKeyboardButton> buttons, int columnCount, State backState) {
        List<List<InlineKeyboardButton>> partition = new ArrayList<>(ListUtils.partition(buttons, columnCount));
        List<InlineKeyboardButton> backButtonRow = List.of(InlineKeyboardButton.builder().text("Назад").callbackData(backState.name()).build());
        partition.add(backButtonRow);
        return partition;
    }


}
