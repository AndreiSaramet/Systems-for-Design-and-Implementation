package ro.ubb.opera.core.service;

import ro.ubb.opera.core.model.Venue;

import java.util.Set;

public interface VenueService extends Service {
    Venue addVenue(Integer seatsNo, Integer floor);

    Venue findVenueById(Integer id);

    Set<Venue> findVenuesBySeatsNo(Integer seatsNo);

    Set<Venue> findVenuesByFloor(Integer floor);

    Set<Venue> getAllVenues();

    Venue updateVenue(Integer id, Integer seatsNo, Integer floor);

    Venue deleteVenue(Integer id);
}
