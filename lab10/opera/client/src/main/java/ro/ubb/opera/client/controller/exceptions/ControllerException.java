package ro.ubb.opera.client.controller.exceptions;

import ro.ubb.opera.common.exception.OperaException;

public class ControllerException extends OperaException {
    public ControllerException(String message) {
        super(message);
    }

    public ControllerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ControllerException(Throwable cause) {
        super(cause);
    }
}
