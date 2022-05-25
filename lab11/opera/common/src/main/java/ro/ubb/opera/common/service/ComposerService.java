package ro.ubb.opera.common.service;

import ro.ubb.opera.common.domain.Composer;
import ro.ubb.opera.common.service.exceptions.ServiceException;

import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.Future;

public interface ComposerService extends Service {
    String ADD_COMPOSER = "addComposer";

    String FIND_COMPOSER_BY_ID = "findComposerById";

    String FIND_COMPOSER_BY_NAME = "findComposerByName";

    String FIND_COMPOSERS_BY_NATIONALITY = "findComposersByNationality";

    String FIND_COMPOSERS_BY_MUSICAL_PERIOD = "findComposersByMusicalPeriod";

    String GET_ALL_COMPOSERS = "getAllComposers";

    String UPDATE_COMPOSER = "updateComposer";

    String DELETE_COMPOSER = "deleteComposer";

    Future<Composer> addComposer(String name, String nationality, String musicalPeriod);

    Future<Composer> findComposerById(Integer id);

    Future<Composer> findComposerByName(String name);

    Future<Set<Composer>> findComposersByNationality(String nationality);

    Future<Set<Composer>> findComposersByMusicalPeriod(String musicalPeriod);

    Future<Set<Composer>> getAllComposers();

    Future<Composer> updateComposer(Integer id, String name, String nationality, String musicalPeriod);

    Future<Composer> deleteComposer(Integer id);

    static String encodeComposer(Composer composer) {
        if (composer == null) {
            return "";
        }
        return composer.getId().toString() + Service.TOKENS_DELIMITER + composer.getName() + Service.TOKENS_DELIMITER + composer.getNationality() + Service.TOKENS_DELIMITER + composer.getMusicalPeriod();
    }

    static Composer decodeComposer(String encodedComposer) {
        StringTokenizer stringTokenizer = new StringTokenizer(encodedComposer, Service.TOKENS_DELIMITER);
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
