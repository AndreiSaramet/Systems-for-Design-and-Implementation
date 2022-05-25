package ro.ubb.opera.common.domain.validators;

import ro.ubb.opera.common.domain.exceptions.ValidatorException;

public interface Validator<T> {
    void validate(T entity) throws ValidatorException;
}
