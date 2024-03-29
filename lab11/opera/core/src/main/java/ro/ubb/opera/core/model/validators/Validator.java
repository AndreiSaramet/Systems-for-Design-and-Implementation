package ro.ubb.opera.core.model.validators;

import ro.ubb.opera.core.model.exceptions.ValidatorException;

public interface Validator<T> {
    void validate(T entity) throws ValidatorException;
}
