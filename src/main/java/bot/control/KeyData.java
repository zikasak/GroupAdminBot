package bot.control;

import bot.control.states.State;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public record KeyData(InlineKeyboardButton button) {
    KeyData(String text, State state) {
        this(InlineKeyboardButton.builder().text(text).callbackData(state.name()).build());
    }
}
