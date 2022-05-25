package ro.ubb.opera.common.service;

import ro.ubb.opera.common.domain.Opera;
import ro.ubb.opera.common.service.exceptions.ServiceException;

import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.Future;

public interface OperaService extends Service {
    String ADD_OPERA = "addOpera";

    String FIND_OPERA_BY_ID = "findOperaById";

    String FIND_OPERA_BY_TITLE = "findOperaByTitle";

    String FIND_OPERAS_BY_LANGUAGE = "findOperasByLanguage";

    String FIND_OPERAS_BY_COMPOSER = "findOperasByComposer";

    String GET_ALL_OPERA = "getAllOperas";

    String UPDATE_OPERA = "updateOpera";

    String DELETE_OPERA = "deleteOpera";

    Future<Opera> addOpera(String title, String language, Integer composerId);

    Future<Opera> findOperaById(Integer id);

    Future<Opera> findOperaByTitle(String title);

    Future<Set<Opera>> findOperasByLanguage(String language);

    Future<Set<Opera>> findOperasByComposer(Integer composerId);

    Future<Set<Opera>> getAllOperas();

    Future<Opera> updateOpera(Integer id, String title, String language, Integer composerId);

    Future<Opera> deleteOpera(Integer id);

    static String encodeOpera(Opera opera) {
        if (opera == null) {
            return "";
        }
        return opera.getId().toString() + Service.TOKENS_DELIMITER + opera.getTitle() + Service.TOKENS_DELIMITER + opera.getLanguage() + Service.TOKENS_DELIMITER + opera.getComposerId().toString();
    }

    static Opera decodeOpera(String encodedOpera) {
        StringTokenizer stringTokenizer = new StringTokenizer(encodedOpera, Service.TOKENS_DELIMITER);
        if (stringTokenizer.countTokens() != 4) {
            throw new ServiceException("error in decoding an opera");
        }
        try {
            return new Opera(Integer.parseInt(stringTokenizer.nextToken()), stringTokenizer.nextToken(), stringTokenizer.nextToken(), Integer.parseInt(stringTokenizer.nextToken()));
        } catch (NumberFormatException e) {
            throw new ServiceException(e);
        }
    }
}
