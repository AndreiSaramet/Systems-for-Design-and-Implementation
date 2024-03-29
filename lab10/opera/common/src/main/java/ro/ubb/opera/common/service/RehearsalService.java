package ro.ubb.opera.common.service;

import ro.ubb.opera.common.domain.Rehearsal;
import ro.ubb.opera.common.service.Service;
import ro.ubb.opera.common.service.exceptions.ServiceException;

import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.Future;

public interface RehearsalService extends Service {
    String ADD_REHEARSAL = "addRehearsal";

    String FIND_REHEARSAL_BY_ID = "findRehearsalById";

    String FIND_REHEARSALS_BY_ORCHESTRA = "findRehearsalsByOrchestra";

    String FIND_REHEARSALS_BY_VENUE = "findRehearsalsByVenue";

    String GET_ALL_REHEARSALS = "getAllRehearsals";

    String UPDATE_REHEARSAL = "updateRehearsal";

    String DELETE_REHEARSAL = "deleteRehearsal";

    Future<Rehearsal> addRehearsal(Integer orchestraId, Integer venueId);

    Future<Rehearsal> findRehearsalById(Integer id);

    Future<Set<Rehearsal>> findRehearsalsByOrchestra(Integer orchestraId);

    Future<Set<Rehearsal>> findRehearsalsByVenue(Integer venueId);

    Future<Set<Rehearsal>> getAllRehearsals();

    Future<Rehearsal> updateRehearsal(Integer id, Integer orchestraId, Integer venueId);

    Future<Rehearsal> deleteRehearsal(Integer id);

    static String encodeRehearsal(Rehearsal rehearsal) {
        if (rehearsal == null) {
            return "";
        }
        return rehearsal.getId().toString() + Service.TOKENS_DELIMITER + rehearsal.getOrchestraId().toString() + Service.TOKENS_DELIMITER + rehearsal.getVenueId().toString();
    }

    static Rehearsal decodeRehearsal(String encodedRehearsal) {
        StringTokenizer stringTokenizer = new StringTokenizer(encodedRehearsal, Service.TOKENS_DELIMITER);
        if (stringTokenizer.countTokens() != 3) {
            throw new ServiceException("error in decoding an opera");
        }
        try {
            return new Rehearsal(Integer.parseInt(stringTokenizer.nextToken()), Integer.parseInt(stringTokenizer.nextToken()), Integer.parseInt(stringTokenizer.nextToken()));
        } catch (NumberFormatException e) {
            throw new ServiceException(e);
        }
    }
}
