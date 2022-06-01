package ro.ubb.opera.core.service;

import ro.ubb.opera.core.model.Composer;
import ro.ubb.opera.core.service.exceptions.ServiceException;

import java.util.Set;
import java.util.StringTokenizer;

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
