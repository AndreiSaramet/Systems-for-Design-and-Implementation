package ro.ubb.opera.common.controller;

import ro.ubb.opera.common.domain.*;

import java.util.Map;
import java.util.Set;

public interface Controller {
    String COMPOSER_ENTITY_NAME = "composer";

    String OPERA_ENTITY_NAME = "opera";

    String ORCHESTRA_ENTITY_NAME = "orchestra";

    String VENUE_ENTITY_NAME = "venue";

    String REHEARSAL_ENTITY_NAME = "rehearsal";

    Composer addComposer(String name, String nationality, String musicalPeriod);

    Opera addOpera(String title, String language, Integer composerId);

    Orchestra addOrchestra(String name, String composer);

    Venue addVenue(Integer seatsNo, Integer floor);

    Rehearsal addRehearsal(Integer orchestraId, Integer venueId);

    Composer findComposerById(Integer id);

    Map.Entry<Opera, Composer> findOperaById(Integer id);

    Orchestra findOrchestraById(Integer id);

    Venue findVenueById(Integer id);

    Map.Entry<Orchestra, Venue> findRehearsalById(Integer id);

    Composer findComposerByName(String name);

    Map.Entry<Opera, Composer> findOperaByTitle(String title);

    Orchestra findOrchestraByName(String name);

    Set<Composer> findComposersByNationality(String nationality);

    Set<Composer> findComposersByMusicalPeriod(String musicalPeriod);

    Set<Map.Entry<Opera, Composer>> findOperasByLanguage(String language);

    Set<Orchestra> findOrchestrasByConductor(String conductor);

    Set<Venue> findVenuesBySeatsNo(Integer seatsNo);

    Set<Venue> findVenuesByFloor(Integer floor);

    Map.Entry<Composer, Set<Opera>> findOperasByComposer(Integer composerId);

    Map.Entry<Orchestra, Set<Venue>> findRehearsalsByOrchestra(Integer orchestraId);

    Map.Entry<Venue, Set<Orchestra>> findRehearsalsByVenue(Integer venueId);

    Set<Composer> getAllComposers();

    Set<Map.Entry<Opera, Composer>> getAllOperas();

    Set<Orchestra> getAllOrchestras();

    Set<Venue> getAllVenues();

    Set<Map.Entry<Orchestra, Venue>> getAllRehearsals();

    Composer updateComposer(Integer id, String name, String nationality, String musicalPeriod);

    Opera updateOpera(Integer id, String title, String language, Integer composerId);

    Orchestra updateOrchestra(Integer id, String name, String conductor);

    Venue updateVenue(Integer id, Integer seatsNo, Integer floor);

    Rehearsal updateRehearsal(Integer id, Integer orchestraId, Integer venueId);

    Composer deleteComposer(Integer id);

    Opera deleteOpera(Integer id);

    Orchestra deleteOrchestra(Integer id);

    Venue deleteVenue(Integer id);

    Rehearsal deleteRehearsal(Integer id);
}
