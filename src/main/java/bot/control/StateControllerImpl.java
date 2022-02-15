package bot.control;

import bot.Constants;
import bot.control.states.State;
import bot.control.states.StateHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class StateControllerImpl implements StateController {

    private final Map<State, StateHandler> handlers;

    public StateControllerImpl(List<StateHandler> handlerList) {
        handlers = handlerList.stream()
                .collect(Collectors.toMap(StateHandler::getHandlingState, Function.identity()));
    }

    @Override
    public void onUpdateReceived(AbsSender sender, Update update, Session chatSession) {
        State state = (State) chatSession.getAttribute(Constants.STATE);
        if (state == null) state = State.UNKNOWN;
        try {
            State resultingState = handlers.get(state).onUpdateReceived(sender, update, chatSession);
            chatSession.setAttribute(Constants.STATE, resultingState);
        } catch (TelegramApiException e) {
            chatSession.getAttributeKeys().forEach(chatSession::removeAttribute);
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }

    }
}
