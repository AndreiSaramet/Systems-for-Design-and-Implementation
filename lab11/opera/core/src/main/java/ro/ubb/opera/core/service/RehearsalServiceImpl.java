package ro.ubb.opera.core.service;

import ro.ubb.opera.core.model.Rehearsal;
import ro.ubb.opera.core.model.validators.Validator;
import ro.ubb.opera.core.repository.Repository;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class RehearsalServiceImpl implements RehearsalService {
    private final ExecutorService executorService;

    private final Validator<Rehearsal> rehearsalValidator;

    private final Repository<Integer, Rehearsal> rehearsalRepository;

    public RehearsalServiceImpl(ExecutorService executorService, Validator<Rehearsal> rehearsalValidator, Repository<Integer, Rehearsal> rehearsalRepository) {
        this.executorService = executorService;
        this.rehearsalValidator = rehearsalValidator;
        this.rehearsalRepository = rehearsalRepository;
    }

    @Override
    public Future<Rehearsal> addRehearsal(Integer orchestraId, Integer venueId) {
        return this.executorService.submit(() -> {
            Rehearsal rehearsal = new Rehearsal(-1, orchestraId, venueId);
            this.rehearsalValidator.validate(rehearsal);
            return this.rehearsalRepository.save(rehearsal).orElse(null);
        });
    }

    @Override
    public Future<Rehearsal> findRehearsalById(Integer id) {
        return this.executorService.submit(() -> this.rehearsalRepository.findOne(id).orElse(null));
    }

    @Override
    public Future<Set<Rehearsal>> findRehearsalsByOrchestra(Integer orchestraId) {
        return this.executorService.submit(() -> {
            Set<Rehearsal> rehearsalSet = new HashSet<>();
            this.rehearsalRepository.findAll().forEach(rehearsalSet::add);
            return rehearsalSet.stream().filter(rehearsal -> rehearsal.getOrchestraId().equals(orchestraId)).collect(Collectors.toSet());
        });
    }

    @Override
    public Future<Set<Rehearsal>> findRehearsalsByVenue(Integer venueId) {
        return this.executorService.submit(() -> {
            Set<Rehearsal> rehearsalSet = new HashSet<>();
            this.rehearsalRepository.findAll().forEach(rehearsalSet::add);
            return rehearsalSet.stream().filter(rehearsal -> rehearsal.getVenueId().equals(venueId)).collect(Collectors.toSet());
        });
    }

    @Override
    public Future<Set<Rehearsal>> getAllRehearsals() {
        return this.executorService.submit(() -> {
            Set<Rehearsal> rehearsalSet = new HashSet<>();
            this.rehearsalRepository.findAll().forEach(rehearsalSet::add);
            return rehearsalSet;
        });
    }

    @Override
    public Future<Rehearsal> updateRehearsal(Integer id, Integer orchestraId, Integer venueId) {
        return this.executorService.submit(() -> {
            Rehearsal rehearsal = new Rehearsal(id, orchestraId, venueId);
            this.rehearsalValidator.validate(rehearsal);
            return this.rehearsalRepository.update(rehearsal).orElse(null);
        });
    }

    @Override
    public Future<Rehearsal> deleteRehearsal(Integer id) {
        return this.executorService.submit(() -> this.rehearsalRepository.delete(id).orElse(null));
    }
}
