package ro.ubb.opera.server.service;

import ro.ubb.opera.common.domain.Orchestra;
import ro.ubb.opera.common.domain.validators.Validator;
import ro.ubb.opera.common.service.OrchestraService;
import ro.ubb.opera.server.repository.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class OrchestraServiceImpl implements OrchestraService {
    private final ExecutorService executorService;

    private final Validator<Orchestra> orchestraValidator;

    private final Repository<Integer, Orchestra> orchestraRepository;

    public OrchestraServiceImpl(ExecutorService executorService, Validator<Orchestra> orchestraValidator, Repository<Integer, Orchestra> orchestraRepository) {
        this.executorService = executorService;
        this.orchestraValidator = orchestraValidator;
        this.orchestraRepository = orchestraRepository;
    }

    @Override
    public Future<Orchestra> addOrchestra(String name, String conductor) {
        return this.executorService.submit(() -> {
            Orchestra orchestra = new Orchestra(-1, name, conductor);
            this.orchestraValidator.validate(orchestra);
            return this.orchestraRepository.save(orchestra).orElse(null);
        });
    }

    @Override
    public Future<Orchestra> findOrchestraById(Integer id) {
        return this.executorService.submit(() -> this.orchestraRepository.findOne(id).orElse(null));
    }

    @Override
    public Future<Orchestra> findOrchestraByName(String name) {
        return this.executorService.submit(() -> {
            Set<Orchestra> orchestraSet = new HashSet<>();
            this.orchestraRepository.findAll().forEach(orchestraSet::add);
            orchestraSet = orchestraSet.stream().filter(orchestra -> orchestra.getName().equals(name)).collect(Collectors.toSet());
            if (orchestraSet.isEmpty()) {
                return null;
            }
            return new ArrayList<>(orchestraSet).get(0);
        });
    }

    @Override
    public Future<Set<Orchestra>> findOrchestrasByConductor(String conductor) {
        return this.executorService.submit(() -> {
            Set<Orchestra> orchestraSet = new HashSet<>();
            this.orchestraRepository.findAll().forEach(orchestraSet::add);
            return orchestraSet.stream().filter(orchestra -> orchestra.getConductor().equals(conductor)).collect(Collectors.toSet());
        });
    }

    @Override
    public Future<Set<Orchestra>> getAllOrchestras() {
        return this.executorService.submit(() -> {
            Set<Orchestra> orchestraSet = new HashSet<>();
            this.orchestraRepository.findAll().forEach(orchestraSet::add);
            return orchestraSet;
        });
    }

    @Override
    public Future<Orchestra> updateOrchestra(Integer id, String name, String conductor) {
        return this.executorService.submit(() -> {
            Orchestra orchestra = new Orchestra(id, name, conductor);
            this.orchestraValidator.validate(orchestra);
            return this.orchestraRepository.update(orchestra).orElse(null);
        });
    }

    @Override
    public Future<Orchestra> deleteOrchestra(Integer id) {
        return this.executorService.submit(() -> this.orchestraRepository.delete(id).orElse(null));
    }
}
