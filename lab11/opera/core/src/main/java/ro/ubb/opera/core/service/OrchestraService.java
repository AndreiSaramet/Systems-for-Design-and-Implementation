package ro.ubb.opera.core.service;

import ro.ubb.opera.core.model.Orchestra;

import java.util.Set;

public interface OrchestraService extends Service {
    Orchestra addOrchestra(String name, String conductor);

    Orchestra findOrchestraById(Integer id);

    Orchestra findOrchestraByName(String name);

    Set<Orchestra> findOrchestrasByConductor(String conductor);

    Set<Orchestra> getAllOrchestras();

    Orchestra updateOrchestra(Integer id, String name, String conductor);

    Orchestra deleteOrchestra(Integer id);
}
