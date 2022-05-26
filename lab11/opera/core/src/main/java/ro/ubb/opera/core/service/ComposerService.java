package ro.ubb.opera.core.service;

import ro.ubb.opera.core.model.Composer;
import ro.ubb.opera.core.service.exceptions.ServiceException;

import java.util.Set;
import java.util.StringTokenizer;

public interface ComposerService extends IService {
    String ADD_COMPOSER = "addComposer";

    String FIND_COMPOSER_BY_ID = "findComposerById";

    String FIND_COMPOSER_BY_NAME = "findComposerByName";

    String FIND_COMPOSERS_BY_NATIONALITY = "findComposersByNationality";

    String FIND_COMPOSERS_BY_MUSICAL_PERIOD = "findComposersByMusicalPeriod";

    String GET_ALL_COMPOSERS = "getAllComposers";

    String UPDATE_COMPOSER = "updateComposer";

    String DELETE_COMPOSER = "deleteComposer";

    Composer addComposer(String name, String nationality, String musicalPeriod);

    Composer findComposerById(Integer id);

    Composer findComposerByName(String name);

    Set<Composer> findComposersByNationality(String nationality);

    Set<Composer> findComposersByMusicalPeriod(String musicalPeriod);

    Set<Composer> getAllComposers();

    Composer updateComposer(Integer id, String name, String nationality, String musicalPeriod);

    Composer deleteComposer(Integer id);

    static String encodeComposer(Composer composer) {
        if (composer == null) {
            return "";
        }
        return composer.getId().toString() + IService.TOKENS_DELIMITER + composer.getName() + IService.TOKENS_DELIMITER + composer.getNationality() + IService.TOKENS_DELIMITER + composer.getMusicalPeriod();
    }

    static Composer decodeComposer(String encodedComposer) {
        StringTokenizer stringTokenizer = new StringTokenizer(encodedComposer, IService.TOKENS_DELIMITER);
        if (stringTokenizer.countTokens() != 4) {
            throw new ServiceException("error in decoding an entity");
        }
        try {
            return new Composer(Integer.parseInt(stringTokenizer.nextToken()), stringTokenizer.nextToken(), stringTokenizer.nextToken(), stringTokenizer.nextToken());
        } catch (NumberFormatException e) {
            throw new ServiceException(e);
        }
    }
}
