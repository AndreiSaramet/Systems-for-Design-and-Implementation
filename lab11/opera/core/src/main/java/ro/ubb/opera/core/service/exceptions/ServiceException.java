package ro.ubb.opera.core.service.exceptions;

import ro.ubb.opera.core.exception.OperaException;

public class ServiceException extends OperaException {
    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }
}
