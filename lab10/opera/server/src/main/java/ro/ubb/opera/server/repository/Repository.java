package ro.ubb.opera.server.repository;

import ro.ubb.opera.common.domain.BaseEntity;
import ro.ubb.opera.common.domain.exceptions.ValidatorException;

import java.io.Serializable;
import java.util.Optional;

public interface Repository<ID extends Serializable, T extends BaseEntity<ID>> {
    Optional<T> findOne(ID id);

    Iterable<T> findAll();

    Optional<T> save(T entity) throws ValidatorException;

    Optional<T> delete(ID id);

    Optional<T> update(T entity) throws ValidatorException;
}
