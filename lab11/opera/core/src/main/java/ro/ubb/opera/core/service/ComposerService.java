package ro.ubb.opera.core.service;

import ro.ubb.opera.core.model.Composer;

import java.util.Set;

public interface ComposerService extends Service {
    Composer addComposer(String name, String nationality, String musicalPeriod);

    Composer findComposerById(Integer id);

    Composer findComposerByName(String name);

    Set<Composer> findComposersByNationality(String nationality);

    Set<Composer> findComposersByMusicalPeriod(String musicalPeriod);

    Set<Composer> getAllComposers();

    Composer updateComposer(Integer id, String name, String nationality, String musicalPeriod);

    Composer deleteComposer(Integer id);
}
