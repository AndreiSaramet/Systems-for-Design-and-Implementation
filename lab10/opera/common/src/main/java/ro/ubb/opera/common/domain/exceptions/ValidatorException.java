package ro.ubb.opera.common.domain.exceptions;

import ro.ubb.opera.common.exception.OperaException;

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
