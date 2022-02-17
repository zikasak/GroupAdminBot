package bot.control;

import bot.control.states.State;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public record KeyData(InlineKeyboardButton button) {
    @JsonCreator
    KeyData(@JsonProperty("text") String text, @JsonProperty("state") State state) {
        this(InlineKeyboardButton.builder().text(text).callbackData(state.name()).build());
    }
}
