package bot.exceptions;

public class WrongStateException extends RuntimeException {

    public WrongStateException(String message, Throwable cause) {
        super(message, cause);
    }
}
