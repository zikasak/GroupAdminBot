package bot.control;

import bot.Constants;
import bot.control.states.State;
import bot.control.states.StateHandler;
import bot.exceptions.WrongStateException;
import bot.utils.TelegramUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class StateControllerImpl implements StateController {

    private final Map<State, StateHandler> handlers;

    public StateControllerImpl(List<StateHandler> handlerList) {
        handlers = handlerList.stream()
                .collect(Collectors.toMap(StateHandler::handlingState, Function.identity()));
    }

    @Override
    public void onUpdateReceived(AbsSender sender, Update update, Session chatSession) {
        Stack<State> states = (Stack<State>) chatSession.getAttribute(Constants.STATE_STACK);
        boolean stateChanged = proceedStateControlMessage(update, states);
        State currentState = states.peek();
        try {
            if (!stateChanged)
                currentState = handlers.get(currentState).handleStateExecution(sender, update, chatSession);
           callStateEnterHandler(currentState, sender, update, chatSession);
            if (!currentState.equals(states.peek()))
                states.push(currentState);
        } catch (TelegramApiException | IOException e) {
            TelegramUtils.clearSession(chatSession);
            states = (Stack<State>) chatSession.getAttribute(Constants.STATE_STACK);
            currentState = states.peek();
            callStateEnterHandler(currentState, sender, update, chatSession);
            log.error(e.getMessage(), e);
        }

    }

    private void callStateEnterHandler(State state, AbsSender sender, Update update, Session session) {
        try {
            handlers.get(state).handleStateEnter(sender, update, session);
        } catch (TelegramApiException | IOException e) {
            throw new WrongStateException(e.getMessage(), e);
        }
    }

    private boolean proceedStateControlMessage(Update update, Stack<State> states) {
        if (!update.hasCallbackQuery()) return false;
        String data = update.getCallbackQuery().getData();
        switch (data) {
            case Constants.RETURN_BACK -> {
                states.pop(); return true;
            }
            default -> states.peek();
        }
        return false;
    }
}
