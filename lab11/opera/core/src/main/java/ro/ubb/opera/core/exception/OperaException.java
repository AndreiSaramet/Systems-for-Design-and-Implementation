package ro.ubb.opera.core.exception;

public class OperaException extends RuntimeException {
    public OperaException(String message) {
        super(message);
    }

    public OperaException(String message, Throwable cause) {
        super(message, cause);
    }

    public OperaException(Throwable cause) {
        super(cause);
    }
}
