package ro.ubb.opera.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.opera.core.model.Venue;
import ro.ubb.opera.core.model.validators.Validator;
import ro.ubb.opera.core.model.validators.VenueValidator;
import ro.ubb.opera.core.repository.Repository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VenueServiceImpl implements VenueService {
    private final Validator<Venue> venueValidator;

    @Autowired
    private final Repository<Integer, Venue> venueRepository;

    public VenueServiceImpl(Repository<Integer, Venue> venueRepository) {
        this.venueValidator = new VenueValidator();
        this.venueRepository = venueRepository;
    }

    @Override
    public Venue addVenue(Integer seatsNo, Integer floor) {
        Venue venue = new Venue(-1, seatsNo, floor);
        this.venueValidator.validate(venue);
        return this.venueRepository.save(venue);
    }

    @Override
    public Venue findVenueById(Integer id) {
        return this.venueRepository.findById(id).orElse(null);
    }

    @Override
    public Set<Venue> findVenuesBySeatsNo(Integer seatsNo) {
        return this.venueRepository.findAll().stream().filter(venue -> venue.getNumberOfSeats().equals(seatsNo)).collect(Collectors.toSet());
    }

    @Override
    public Set<Venue> findVenuesByFloor(Integer floor) {
        return this.venueRepository.findAll().stream().filter(venue -> venue.getFloor().equals(floor)).collect(Collectors.toSet());
    }

    @Override
    public Set<Venue> getAllVenues() {
        return new HashSet<>(this.venueRepository.findAll());
    }

    @Override
    @Transactional
    public Venue updateVenue(Integer id, Integer seatsNo, Integer floor) {
        Venue venue = this.venueRepository.findById(id).orElse(null);
        if (venue == null) {
            return null;
        }
        venue.setNumberOfSeats(seatsNo);
        venue.setFloor(floor);
        this.venueValidator.validate(venue);
        return venue;
    }

    @Override
    public Venue deleteVenue(Integer id) {
        Venue venue = this.venueRepository.findById(id).orElse(null);
        this.venueRepository.deleteById(id);
        return venue;
    }
}
