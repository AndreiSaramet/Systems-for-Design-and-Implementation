package ro.ubb.opera.client.controller;

import ro.ubb.opera.client.controller.exceptions.ControllerException;
import ro.ubb.opera.common.controller.Controller;
import ro.ubb.opera.common.domain.*;
import ro.ubb.opera.common.service.*;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class ClientController implements Controller {
    private final ComposerService composerService;

    private final OperaService operaService;

    private final OrchestraService orchestraService;

    private final VenueService venueService;

    private final RehearsalService rehearsalService;

    public ClientController(ComposerService composerService, OperaService operaService, OrchestraService orchestraService, VenueService venueService, RehearsalService rehearsalService) {
        this.composerService = composerService;
        this.operaService = operaService;
        this.orchestraService = orchestraService;
        this.venueService = venueService;
        this.rehearsalService = rehearsalService;
    }

    @Override
    public Composer addComposer(String name, String nationality, String musicalPeriod) {
        Future<Composer> result = this.composerService.addComposer(name, nationality, musicalPeriod);
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Opera addOpera(String title, String language, Integer composerId) {
        Future<Composer> composerFuture = this.composerService.findComposerById(composerId);
        try {
            if (composerFuture.get() == null) {
                throw new ControllerException("There is no composer with the given composer id");
            }
            Future<Opera> result = this.operaService.addOpera(title, language, composerId);
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Orchestra addOrchestra(String name, String composer) {
        Future<Orchestra> result = this.orchestraService.addOrchestra(name, composer);
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Venue addVenue(Integer seatsNo, Integer floor) {
        Future<Venue> result = this.venueService.addVenue(seatsNo, floor);
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Rehearsal addRehearsal(Integer orchestraId, Integer venueId) {
        Future<Orchestra> orchestraFuture = this.orchestraService.findOrchestraById(orchestraId);
        Future<Venue> venueFuture = this.venueService.findVenueById(venueId);
        try {
            if (orchestraFuture.get() == null) {
                throw new ControllerException("There is no orchestra with the given id");
            }
            if (venueFuture.get() == null) {
                throw new ControllerException("There is not venue with the given id");
            }
            Future<Rehearsal> result = this.rehearsalService.addRehearsal(orchestraId, venueId);
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Composer findComposerById(Integer id) {
        Future<Composer> result = this.composerService.findComposerById(id);
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Map.Entry<Opera, Composer> findOperaById(Integer id) {
        Future<Opera> operaFuture = this.operaService.findOperaById(id);
        try {
            Future<Composer> composerFuture = this.composerService.findComposerById(operaFuture.get().getComposerId());
            return new AbstractMap.SimpleEntry<>(operaFuture.get(), composerFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Orchestra findOrchestraById(Integer id) {
        Future<Orchestra> result = this.orchestraService.findOrchestraById(id);
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Venue findVenueById(Integer id) {
        Future<Venue> result = this.venueService.findVenueById(id);
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Map.Entry<Orchestra, Venue> findRehearsalById(Integer id) {
        Future<Rehearsal> rehearsalFuture = this.rehearsalService.findRehearsalById(id);
        try {
            Future<Orchestra> orchestraFuture = this.orchestraService.findOrchestraById(rehearsalFuture.get().getOrchestraId());
            Future<Venue> venueFuture = this.venueService.findVenueById(rehearsalFuture.get().getVenueId());
            return new AbstractMap.SimpleEntry<>(orchestraFuture.get(), venueFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Composer findComposerByName(String name) {
        Future<Composer> result = this.composerService.findComposerByName(name);
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Map.Entry<Opera, Composer> findOperaByTitle(String title) {
        Future<Opera> operaFuture = this.operaService.findOperaByTitle(title);
        try {
            if (operaFuture.get() == null) {
                return null;
            }
            Future<Composer> composerFuture = this.composerService.findComposerById(operaFuture.get().getComposerId());
            return new AbstractMap.SimpleEntry<>(operaFuture.get(), composerFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Orchestra findOrchestraByName(String name) {
        Future<Orchestra> result = this.orchestraService.findOrchestraByName(name);
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Set<Composer> findComposersByNationality(String nationality) {
        Future<Set<Composer>> result = this.composerService.findComposersByNationality(nationality);
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Set<Composer> findComposersByMusicalPeriod(String musicalPeriod) {
        Future<Set<Composer>> result = this.composerService.findComposersByMusicalPeriod(musicalPeriod);
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Set<Map.Entry<Opera, Composer>> findOperasByLanguage(String language) {
        Future<Set<Opera>> result = this.operaService.findOperasByLanguage(language);
        try {
            return result.get().stream().map(opera -> {
                try {
                    return new AbstractMap.SimpleEntry<>(opera, this.composerService.findComposerById(opera.getComposerId()).get());
                } catch (InterruptedException | ExecutionException e) {
                    throw new ControllerException(e);
                }
            }).collect(Collectors.toSet());
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Set<Orchestra> findOrchestrasByConductor(String conductor) {
        Future<Set<Orchestra>> result = this.orchestraService.findOrchestrasByConductor(conductor);
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Set<Venue> findVenuesBySeatsNo(Integer seatsNo) {
        Future<Set<Venue>> result = this.venueService.findVenuesBySeatsNo(seatsNo);
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Set<Venue> findVenuesByFloor(Integer floor) {
        Future<Set<Venue>> result = this.venueService.findVenuesByFloor(floor);
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Map.Entry<Composer, Set<Opera>> findOperasByComposer(Integer composerId) {
        Future<Composer> composerFuture = this.composerService.findComposerById(composerId);
        Future<Set<Opera>> operaSetFuture = this.operaService.findOperasByComposer(composerId);
        try {
            return new AbstractMap.SimpleEntry<>(composerFuture.get(), operaSetFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Map.Entry<Orchestra, Set<Venue>> findRehearsalsByOrchestra(Integer orchestraId) {
        Future<Orchestra> orchestraFuture = this.orchestraService.deleteOrchestra(orchestraId);
        Future<Set<Rehearsal>> rehearsalSetFuture = this.rehearsalService.findRehearsalsByOrchestra(orchestraId);
        try {
            return new AbstractMap.SimpleEntry<>(orchestraFuture.get(), rehearsalSetFuture.get().stream().map(Rehearsal::getVenueId).map(this.venueService::findVenueById).map(future -> {
                try {
                    return future.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new ControllerException(e);
                }
            }).collect(Collectors.toSet()));
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Map.Entry<Venue, Set<Orchestra>> findRehearsalsByVenue(Integer venueId) {
        Future<Venue> venueFuture = this.venueService.findVenueById(venueId);
        Future<Set<Rehearsal>> rehearsalSetFuture = this.rehearsalService.findRehearsalsByVenue(venueId);
        try {
            return new AbstractMap.SimpleEntry<>(venueFuture.get(), rehearsalSetFuture.get().stream().map(Rehearsal::getOrchestraId).map(this.orchestraService::findOrchestraById).map(future -> {
                try {
                    return future.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new ControllerException(e);
                }
            }).collect(Collectors.toSet()));
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Set<Composer> getAllComposers() {
        Future<Set<Composer>> result = this.composerService.getAllComposers();
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Set<Map.Entry<Opera, Composer>> getAllOperas() {
        Future<Set<Opera>> result = this.operaService.getAllOperas();
        try {
            return result.get().stream().map(opera -> {
                        try {
                            return new AbstractMap.SimpleEntry<>(opera, this.composerService.findComposerById(opera.getComposerId()).get());
                        } catch (InterruptedException | ExecutionException e) {
                            throw new ControllerException(e);
                        }
                    }
            ).collect(Collectors.toSet());
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Set<Orchestra> getAllOrchestras() {
        Future<Set<Orchestra>> result = this.orchestraService.getAllOrchestras();
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Set<Venue> getAllVenues() {
        Future<Set<Venue>> result = this.venueService.getAllVenues();
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Set<Map.Entry<Orchestra, Venue>> getAllRehearsals() {
        Future<Set<Rehearsal>> resultSet = this.rehearsalService.getAllRehearsals();
        try {
            return resultSet.get().stream().map(rehearsal -> {
                try {
                    return new AbstractMap.SimpleEntry<>(this.orchestraService.findOrchestraById(rehearsal.getOrchestraId()).get(), this.venueService.findVenueById(rehearsal.getVenueId()).get());
                } catch (InterruptedException | ExecutionException e) {
                    throw new ControllerException(e);
                }
            }).collect(Collectors.toSet());
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Composer updateComposer(Integer id, String name, String nationality, String musicalPeriod) {
        Future<Composer> result = this.composerService.updateComposer(id, name, nationality, musicalPeriod);
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Opera updateOpera(Integer id, String title, String language, Integer composerId) {
        Future<Opera> result = this.operaService.updateOpera(id, title, language, composerId);
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Orchestra updateOrchestra(Integer id, String name, String conductor) {
        Future<Orchestra> result = this.orchestraService.updateOrchestra(id, name, conductor);
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Venue updateVenue(Integer id, Integer seatsNo, Integer floor) {
        Future<Venue> result = this.venueService.updateVenue(id, seatsNo, floor);
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Rehearsal updateRehearsal(Integer id, Integer orchestraId, Integer venueId) {
        Future<Rehearsal> result = this.rehearsalService.updateRehearsal(id, orchestraId, venueId);
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Composer deleteComposer(Integer id) {
        Future<Composer> result = this.composerService.deleteComposer(id);
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Opera deleteOpera(Integer id) {
        Future<Opera> result = this.operaService.deleteOpera(id);
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Orchestra deleteOrchestra(Integer id) {
        Future<Orchestra> result = this.orchestraService.deleteOrchestra(id);
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Venue deleteVenue(Integer id) {
        Future<Venue> result = this.venueService.deleteVenue(id);
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }

    @Override
    public Rehearsal deleteRehearsal(Integer id) {
        Future<Rehearsal> result = this.rehearsalService.deleteRehearsal(id);
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new ControllerException(e);
        }
    }
}
