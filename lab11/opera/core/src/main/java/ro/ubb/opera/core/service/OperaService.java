package ro.ubb.opera.core.service;

import ro.ubb.opera.core.model.Opera;

import java.util.Set;

public interface OperaService extends Service {
    Opera addOpera(String title, String language, Integer composerId);

    Opera findOperaById(Integer id);

    Opera findOperaByTitle(String title);

    Set<Opera> findOperasByLanguage(String language);

    Set<Opera> findOperasByComposer(Integer composerId);

    Set<Opera> getAllOperas();

    Opera updateOpera(Integer id, String title, String language, Integer composerId);

    Opera deleteOpera(Integer id);
}
