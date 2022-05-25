package ro.ubb.opera.server.service;

import ro.ubb.opera.common.domain.Composer;
import ro.ubb.opera.common.domain.validators.Validator;
import ro.ubb.opera.common.service.ComposerService;
import ro.ubb.opera.server.repository.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;


public class ComposerServiceImpl implements ComposerService {
    private final ExecutorService executorService;

    private final Validator<Composer> composerValidator;
    private final Repository<Integer, Composer> composerRepository;


    public ComposerServiceImpl(ExecutorService executorService, Validator<Composer> composerValidator, Repository<Integer, Composer> composerRepository) {
        this.executorService = executorService;
        this.composerValidator = composerValidator;
        this.composerRepository = composerRepository;
    }

    @Override
    public Future<Composer> addComposer(String name, String nationality, String musicalPeriod) {
        return this.executorService.submit(() -> {
            Composer composer = new Composer(-1, name, nationality, musicalPeriod);
            this.composerValidator.validate(composer);
            return this.composerRepository.save(composer).orElse(null);
        });
    }

    @Override
    public Future<Composer> findComposerById(Integer id) {
        return this.executorService.submit(() -> this.composerRepository.findOne(id).orElse(null));
    }

    @Override
    public Future<Composer> findComposerByName(String name) {
        return this.executorService.submit(() -> {
            Set<Composer> composerSet = new HashSet<>();
            this.composerRepository.findAll().forEach(composerSet::add);
            composerSet = composerSet.stream().filter(composer -> composer.getName().equals(name)).collect(Collectors.toSet());
            if (composerSet.isEmpty()) {
                return null;
            }
            return new ArrayList<>(composerSet).get(0);
        });
    }

    @Override
    public Future<Set<Composer>> findComposersByNationality(String nationality) {
        return this.executorService.submit(() -> {
            Set<Composer> composerSet = new HashSet<>();
            this.composerRepository.findAll().forEach(composerSet::add);
            return composerSet.stream().filter(composer -> composer.getNationality().equals(nationality)).collect(Collectors.toSet());
        });
    }

    @Override
    public Future<Set<Composer>> findComposersByMusicalPeriod(String musicalPeriod) {
        return this.executorService.submit(() -> {
            Set<Composer> composerSet = new HashSet<>();
            this.composerRepository.findAll().forEach(composerSet::add);
            return composerSet.stream().filter(composer -> composer.getMusicalPeriod().equals(musicalPeriod)).collect(Collectors.toSet());
        });
    }

    @Override
    public Future<Set<Composer>> getAllComposers() {
        return this.executorService.submit(() -> {
            Set<Composer> composerSet = new HashSet<>();
            this.composerRepository.findAll().forEach(composerSet::add);
            return composerSet;
        });
    }

    @Override
    public Future<Composer> updateComposer(Integer id, String name, String nationality, String musicalPeriod) {
        return this.executorService.submit(() -> {
            Composer composer = new Composer(id, name, nationality, musicalPeriod);
            this.composerValidator.validate(composer);
            return this.composerRepository.update(composer).orElse(null);
        });
    }

    @Override
    public Future<Composer> deleteComposer(Integer id) {
        return this.executorService.submit(() -> this.composerRepository.delete(id).orElse(null));
    }
}
