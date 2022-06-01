package ro.ubb.opera.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.opera.core.model.Rehearsal;
import ro.ubb.opera.core.model.validators.RehearsalValidator;
import ro.ubb.opera.core.model.validators.Validator;
import ro.ubb.opera.core.repository.Repository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RehearsalServiceImpl implements RehearsalService {
    private final Validator<Rehearsal> rehearsalValidator;

    @Autowired
    private final Repository<Integer, Rehearsal> rehearsalRepository;

    public RehearsalServiceImpl(Repository<Integer, Rehearsal> rehearsalRepository) {
        this.rehearsalValidator = new RehearsalValidator();
        this.rehearsalRepository = rehearsalRepository;
    }

    @Override
    public Rehearsal addRehearsal(Integer orchestraId, Integer venueId) {
        Rehearsal rehearsal = new Rehearsal(-1, orchestraId, venueId);
        this.rehearsalValidator.validate(rehearsal);
        return this.rehearsalRepository.save(rehearsal);
    }

    @Override
    public Rehearsal findRehearsalById(Integer id) {
        return this.rehearsalRepository.findById(id).orElse(null);
    }

    @Override
    public Set<Rehearsal> findRehearsalsByOrchestra(Integer orchestraId) {
        return this.rehearsalRepository.findAll().stream().filter(rehearsal -> rehearsal.getOrchestraId().equals(orchestraId)).collect(Collectors.toSet());
    }

    @Override
    public Set<Rehearsal> findRehearsalsByVenue(Integer venueId) {
        return this.rehearsalRepository.findAll().stream().filter(rehearsal -> rehearsal.getVenueId().equals(venueId)).collect(Collectors.toSet());
    }

    @Override
    public Set<Rehearsal> getAllRehearsals() {
        return new HashSet<>(this.rehearsalRepository.findAll());
    }

    @Override
    @Transactional
    public Rehearsal updateRehearsal(Integer id, Integer orchestraId, Integer venueId) {
        Rehearsal rehearsal = this.rehearsalRepository.findById(id).orElse(null);
        if (rehearsal == null) {
            return null;
        }
        rehearsal.setOrchestraId(orchestraId);
        rehearsal.setVenueId(venueId);
        this.rehearsalValidator.validate(rehearsal);
        return rehearsal;
    }

    @Override
    public Rehearsal deleteRehearsal(Integer id) {
        Rehearsal rehearsal = this.rehearsalRepository.findById(id).orElse(null);
        this.rehearsalRepository.deleteById(id);
        return rehearsal;
    }
}
