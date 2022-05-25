package ro.ubb.opera.core.service;

import ro.ubb.opera.common.domain.Orchestra;
import ro.ubb.opera.common.service.exceptions.ServiceException;

import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.Future;

public interface OrchestraService extends Service {
    String ADD_ORCHESTRA = "addOrchestra";

    String FIND_ORCHESTRA_BY_ID = "findOrchestraById";

    String FIND_ORCHESTRA_BY_NAME = "findOrchestraByName";

    String FIND_ORCHESTRAS_BY_CONDUCTOR = "findOrchestrasByConductor";

    String GET_ALL_ORCHESTRAS = "getAllOrchestras";

    String UPDATE_ORCHESTRA = "updateOrchestra";

    String DELETE_ORCHESTRA = "deleteOrchestra";

    Future<Orchestra> addOrchestra(String name, String conductor);

    Future<Orchestra> findOrchestraById(Integer id);

    Future<Orchestra> findOrchestraByName(String name);

    Future<Set<Orchestra>> findOrchestrasByConductor(String conductor);

    Future<Set<Orchestra>> getAllOrchestras();

    Future<Orchestra> updateOrchestra(Integer id, String name, String conductor);

    Future<Orchestra> deleteOrchestra(Integer id);

    static String encodeOrchestra(Orchestra orchestra) {
        if (orchestra == null) {
            return "";
        }
        return orchestra.getId().toString() + Service.TOKENS_DELIMITER + orchestra.getName() + Service.TOKENS_DELIMITER + orchestra.getConductor();
    }

    static Orchestra decodeOrchestra(String encodedOrchestra) {
        StringTokenizer stringTokenizer = new StringTokenizer(encodedOrchestra, Service.TOKENS_DELIMITER);
        if (stringTokenizer.countTokens() != 3) {
            throw new ServiceException("error in decoding an entity");
        }
        try {
            return new Orchestra(Integer.parseInt(stringTokenizer.nextToken()), stringTokenizer.nextToken(), stringTokenizer.nextToken());
        } catch (NumberFormatException e) {
            throw new ServiceException(e);
        }
    }
}
