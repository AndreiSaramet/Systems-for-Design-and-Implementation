package ro.ubb.opera.core.model.exceptions;

import ro.ubb.opera.core.exception.OperaException;

public class ValidatorException extends OperaException {
    public ValidatorException(String message) {
        super(message);
    }

    public ValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidatorException(Throwable cause) {
        super(cause);
    }
}
