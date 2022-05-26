package ro.ubb.opera.core.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.ubb.opera.core.model.Composer;
import ro.ubb.opera.core.model.validators.ComposerValidator;
import ro.ubb.opera.core.model.validators.Validator;
import ro.ubb.opera.core.repository.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Service
public class ComposerServiceImpl implements ComposerService {

    private final Validator<Composer> composerValidator;

    @Autowired
    private final Repository<Integer, Composer> composerRepository;


    public ComposerServiceImpl(Repository<Integer, Composer> composerRepository) {
        this.composerValidator = new ComposerValidator();
        this.composerRepository = composerRepository;
    }

    @Override
    public Composer addComposer(String name, String nationality, String musicalPeriod) {
        Composer composer = new Composer(-1, name, nationality, musicalPeriod);
        this.composerValidator.validate(composer);
        return this.composerRepository.save(composer).orElse(null);

    }

    @Override
    public Composer findComposerById(Integer id) {
        return this.composerRepository.findOne(id).orElse(null);
    }

    @Override
    public Composer findComposerByName(String name) {
        Set<Composer> composerSet = new HashSet<>();
        this.composerRepository.findAll().forEach(composerSet::add);
        composerSet = composerSet.stream().filter(composer -> composer.getName().equals(name)).collect(Collectors.toSet());
        if (composerSet.isEmpty()) {
            return null;
        }
        return new ArrayList<>(composerSet).get(0);
    }

    @Override
    public Set<Composer> findComposersByNationality(String nationality) {
        Set<Composer> composerSet = new HashSet<>();
        this.composerRepository.findAll().forEach(composerSet::add);
        return composerSet.stream().filter(composer -> composer.getNationality().equals(nationality)).collect(Collectors.toSet());

    }

    @Override
    public Set<Composer> findComposersByMusicalPeriod(String musicalPeriod) {
        Set<Composer> composerSet = new HashSet<>();
        this.composerRepository.findAll().forEach(composerSet::add);
        return composerSet.stream().filter(composer -> composer.getMusicalPeriod().equals(musicalPeriod)).collect(Collectors.toSet());
    }

    @Override
    public Set<Composer> getAllComposers() {
        Set<Composer> composerSet = new HashSet<>();
        this.composerRepository.findAll().forEach(composerSet::add);
        return composerSet;
    }

    @Override
    public Composer updateComposer(Integer id, String name, String nationality, String musicalPeriod) {
        Composer composer = new Composer(id, name, nationality, musicalPeriod);
        this.composerValidator.validate(composer);
        return this.composerRepository.update(composer).orElse(null);
    }

    @Override
    public Composer deleteComposer(Integer id) {
        return this.composerRepository.delete(id).orElse(null);
    }
}
