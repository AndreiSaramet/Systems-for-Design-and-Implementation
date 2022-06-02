package ro.ubb.opera.core.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.opera.core.model.Composer;
import ro.ubb.opera.core.model.validators.ComposerValidator;
import ro.ubb.opera.core.model.validators.Validator;
import ro.ubb.opera.core.repository.ComposerRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ComposerServiceImpl implements ComposerService {
    private final Validator<Composer> composerValidator;

    @Autowired
    private final ComposerRepository composerRepository;

    public ComposerServiceImpl(ComposerRepository composerRepository) {
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
    public Composer addComposer(Composer composer) {
        this.composerValidator.validate(composer);
        return this.composerRepository.save(composer);
    }

    @Override
    public Composer findComposerById(Integer id) {
        return this.composerRepository.findById(id).orElse(null);
    }

    @Override
    public Composer findComposerByName(String name) {
//        return this.composerRepository.findAll().stream().filter(composer -> composer.getName().equals(name)).findFirst().orElse(null);
        return this.composerRepository.findByName(name).stream().findFirst().orElse(null);
    }

    @Override
    public Set<Composer> findComposersByNationality(String nationality) {
//        return this.composerRepository.findAll().stream().filter(composer -> composer.getNationality().equals(nationality)).collect(Collectors.toSet());
        return new HashSet<>(this.composerRepository.findByNationality(nationality));
    }

    @Override
    public Set<Composer> findComposersByMusicalPeriod(String musicalPeriod) {
//        return this.composerRepository.findAll().stream().filter(composer -> composer.getMusicalPeriod().equals(musicalPeriod)).collect(Collectors.toSet());
        return new HashSet<>(this.composerRepository.findByMusicalPeriod(musicalPeriod));
    }

    @Override
    public Set<Composer> getAllComposers() {
        return new HashSet<>(this.composerRepository.findAll());
    }

    @Override
    public List<Composer> getAllComposersSortedByName(boolean ascending) {
        if (ascending) {
            return this.composerRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
        }
        return this.composerRepository.findAll(new Sort(Sort.Direction.DESC, "name"));
    }

    @Override
    public List<Composer> getAllComposersSortedByNationality(boolean ascending) {
        if (ascending) {
            return this.composerRepository.findAll(new Sort(Sort.Direction.ASC, "nationality"));
        }
        return this.composerRepository.findAll(new Sort(Sort.Direction.DESC, "nationality"));
    }

    @Override
    public List<Composer> getAllComposersSortedByMusicalPeriod(boolean ascending) {
        if (ascending) {
            return this.composerRepository.findAll(new Sort(Sort.Direction.ASC, "musicalPeriod"));
        }
        return this.composerRepository.findAll(new Sort(Sort.Direction.DESC, "musicalPeriod"));
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
    @Transactional
    public Composer updateComposer(Composer composer) {
        Composer updateComposer = this.composerRepository.findById(composer.getId()).orElse(null);
        if (updateComposer == null) {
            return null;
        }
        updateComposer.setName(composer.getName());
        updateComposer.setNationality(composer.getNationality());
        updateComposer.setMusicalPeriod(composer.getMusicalPeriod());
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
