package ro.ubb.opera.core.service;

import ro.ubb.opera.core.model.Venue;
import ro.ubb.opera.core.model.validators.Validator;
import ro.ubb.opera.core.repository.Repository;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class VenueServiceImpl implements VenueService {
    private final ExecutorService executorService;

    private final Validator<Venue> venueValidator;

    private final Repository<Integer, Venue> venueRepository;

    public VenueServiceImpl(ExecutorService executorService, Validator<Venue> venueValidator, Repository<Integer, Venue> venueRepository) {
        this.executorService = executorService;
        this.venueValidator = venueValidator;
        this.venueRepository = venueRepository;
    }

    @Override
    public Future<Venue> addVenue(Integer seatsNo, Integer floor) {
        return this.executorService.submit(() -> {
            Venue venue = new Venue(-1, seatsNo, floor);
            this.venueValidator.validate(venue);
            return this.venueRepository.save(venue).orElse(null);
        });
    }

    @Override
    public Future<Venue> findVenueById(Integer id) {
        return this.executorService.submit(() -> this.venueRepository.findOne(id).orElse(null));
    }

    @Override
    public Future<Set<Venue>> findVenuesBySeatsNo(Integer seatsNo) {
        return this.executorService.submit(() -> {
            Set<Venue> venueSet = new HashSet<>();
            this.venueRepository.findAll().forEach(venueSet::add);
            return venueSet.stream().filter(venue -> venue.getNumberOfSeats().equals(seatsNo)).collect(Collectors.toSet());
        });
    }

    @Override
    public Future<Set<Venue>> findVenuesByFloor(Integer floor) {
        return this.executorService.submit(() -> {
            Set<Venue> venueSet = new HashSet<>();
            this.venueRepository.findAll().forEach(venueSet::add);
            return venueSet.stream().filter(venue -> venue.getFloor().equals(floor)).collect(Collectors.toSet());
        });
    }

    @Override
    public Future<Set<Venue>> getAllVenues() {
        return this.executorService.submit(() -> {
            Set<Venue> venueSet = new HashSet<>();
            this.venueRepository.findAll().forEach(venueSet::add);
            return venueSet;
        });
    }

    @Override
    public Future<Venue> updateVenue(Integer id, Integer seatsNo, Integer floor) {
        return this.executorService.submit(() -> {
            Venue venue = new Venue(id, seatsNo, floor);
            this.venueValidator.validate(venue);
            return this.venueRepository.update(venue).orElse(null);
        });
    }

    @Override
    public Future<Venue> deleteVenue(Integer id) {
        return this.executorService.submit(() -> this.venueRepository.delete(id).orElse(null));
    }
}
