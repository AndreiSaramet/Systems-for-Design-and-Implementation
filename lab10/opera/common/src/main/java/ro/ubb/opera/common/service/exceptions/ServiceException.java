package ro.ubb.opera.common.service.exceptions;

import ro.ubb.opera.common.exception.OperaException;

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
