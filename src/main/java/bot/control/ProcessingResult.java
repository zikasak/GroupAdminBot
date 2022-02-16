package bot.control;

import bot.control.states.State;

public record ProcessingResult(State state, boolean callImmediately) {

    public static ProcessingResult immediately(State state) {
        return new ProcessingResult(state, true);
    }

    public static ProcessingResult later(State state) {
        return new ProcessingResult(state, false);
    }
}
