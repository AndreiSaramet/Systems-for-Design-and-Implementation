package ro.ubb.opera.core.service;

import ro.ubb.opera.core.model.Rehearsal;

import java.util.Set;

public interface RehearsalService extends Service {
    Rehearsal addRehearsal(Integer orchestraId, Integer venueId);

    Rehearsal findRehearsalById(Integer id);

    Set<Rehearsal> findRehearsalsByOrchestra(Integer orchestraId);

    Set<Rehearsal> findRehearsalsByVenue(Integer venueId);

    Set<Rehearsal> getAllRehearsals();

    Rehearsal updateRehearsal(Integer id, Integer orchestraId, Integer venueId);

    Rehearsal deleteRehearsal(Integer id);
}
