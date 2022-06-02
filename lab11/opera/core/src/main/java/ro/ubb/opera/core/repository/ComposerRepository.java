package ro.ubb.opera.core.repository;

import ro.ubb.opera.core.model.Composer;

import java.util.List;

public interface ComposerRepository extends Repository<Integer, Composer> {
    List<Composer> findByName(String name);

    List<Composer> findByNationality(String nationality);

    List<Composer> findByMusicalPeriod(String musicalPeriod);
}
