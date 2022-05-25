package ro.ubb.opera.core.service;

import ro.ubb.opera.common.domain.Venue;
import ro.ubb.opera.common.service.exceptions.ServiceException;

import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.Future;

public interface VenueService extends Service {
    String ADD_VENUE = "addVenue";

    String FIND_VENUE_BY_ID = "findVenueById";

    String FIND_VENUES_BY_SEATS_NO = "findVenuesBySeatsNo";

    String FIND_VENUES_BY_FLOOR = "findVenuesByFloor";

    String GET_ALL_VENUES = "getAllVenues";

    String UPDATE_VENUE = "updateVenue";

    String DELETE_VENUE = "deleteVenue";

    Future<Venue> addVenue(Integer seatsNo, Integer floor);

    Future<Venue> findVenueById(Integer id);

    Future<Set<Venue>> findVenuesBySeatsNo(Integer seatsNo);

    Future<Set<Venue>> findVenuesByFloor(Integer floor);

    Future<Set<Venue>> getAllVenues();

    Future<Venue> updateVenue(Integer id, Integer seatsNo, Integer floor);

    Future<Venue> deleteVenue(Integer id);

    static String encodeVenue(Venue venue) {
        if (venue == null) {
            return "";
        }
        return venue.getId().toString() + Service.TOKENS_DELIMITER + venue.getNumberOfSeats().toString() + Service.TOKENS_DELIMITER + venue.getFloor().toString();
    }

    static Venue decodeVenue(String encodedVenue) {
        StringTokenizer stringTokenizer = new StringTokenizer(encodedVenue, Service.TOKENS_DELIMITER);
        if (stringTokenizer.countTokens() != 3) {
            throw new ServiceException("error in decoding an entity");
        }
        try {
            return new Venue(Integer.parseInt(stringTokenizer.nextToken()), Integer.parseInt(stringTokenizer.nextToken()), Integer.parseInt(stringTokenizer.nextToken()));
        } catch (NumberFormatException e) {
            throw new ServiceException(e);
        }
    }
}
