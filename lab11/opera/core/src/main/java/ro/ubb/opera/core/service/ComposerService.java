package ro.ubb.opera.core.service;

import ro.ubb.opera.core.model.Composer;

import java.util.List;
import java.util.Set;

public interface ComposerService extends Service {
    Composer addComposer(String name, String nationality, String musicalPeriod);

    Composer addComposer(Composer composer);

    Composer findComposerById(Integer id);

    Composer findComposerByName(String name);

    Set<Composer> findComposersByNationality(String nationality);

    Set<Composer> findComposersByMusicalPeriod(String musicalPeriod);

    Set<Composer> getAllComposers();

    List<Composer> getAllComposersSortedByName(boolean ascending);

    List<Composer> getAllComposersSortedByNationality(boolean ascending);

    List<Composer> getAllComposersSortedByMusicalPeriod(boolean ascending);

    Composer updateComposer(Integer id, String name, String nationality, String musicalPeriod);

    Composer updateComposer(Composer composer);

    Composer deleteComposer(Integer id);
}
