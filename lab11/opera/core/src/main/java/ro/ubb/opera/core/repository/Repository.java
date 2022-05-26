package ro.ubb.opera.core.repository;

import org.springframework.data.repository.NoRepositoryBean;
import ro.ubb.opera.core.model.BaseEntity;
import ro.ubb.opera.core.model.exceptions.ValidatorException;

import java.io.Serializable;
import java.util.Optional;

@NoRepositoryBean
public interface Repository<ID extends Serializable, T extends BaseEntity<ID>> {
    Optional<T> findOne(ID id);

    Iterable<T> findAll();

    Optional<T> save(T entity) throws ValidatorException;

    Optional<T> delete(ID id);

    Optional<T> update(T entity) throws ValidatorException;
}
