package ro.ubb.opera.core.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.opera.core.model.Composer;
import ro.ubb.opera.core.model.validators.ComposerValidator;
import ro.ubb.opera.core.model.validators.Validator;
import ro.ubb.opera.core.repository.Repository;

import java.util.HashSet;
import java.util.Set;
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
        return this.composerRepository.save(composer);
    }

    @Override
    public Composer findComposerById(Integer id) {
        return this.composerRepository.findById(id).orElse(null);
    }

    @Override
    public Composer findComposerByName(String name) {
        return this.composerRepository.findAll().stream().filter(composer -> composer.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public Set<Composer> findComposersByNationality(String nationality) {
        return this.composerRepository.findAll().stream().filter(composer -> composer.getNationality().equals(nationality)).collect(Collectors.toSet());
    }

    @Override
    public Set<Composer> findComposersByMusicalPeriod(String musicalPeriod) {
        return this.composerRepository.findAll().stream().filter(composer -> composer.getMusicalPeriod().equals(musicalPeriod)).collect(Collectors.toSet());
    }

    @Override
    public Set<Composer> getAllComposers() {
        return new HashSet<>(this.composerRepository.findAll());
    }

    @Override
    @Transactional
    public Composer updateComposer(Integer id, String name, String nationality, String musicalPeriod) {
        Composer composer = this.composerRepository.findById(id).orElse(null);
        if (composer == null) {
            return null;
        }
        composer.setName(name);
        composer.setNationality(nationality);
        composer.setMusicalPeriod(musicalPeriod);
        this.composerValidator.validate(composer);
        return composer;
    }

    @Override
    public Composer deleteComposer(Integer id) {
        Composer composer = this.composerRepository.findById(id).orElse(null);
        this.composerRepository.deleteById(id);
        return composer;
    }
}
