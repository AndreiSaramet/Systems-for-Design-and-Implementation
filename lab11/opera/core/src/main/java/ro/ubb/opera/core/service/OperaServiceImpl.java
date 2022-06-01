package ro.ubb.opera.core.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.opera.core.model.Opera;
import ro.ubb.opera.core.model.validators.OperaValidator;
import ro.ubb.opera.core.model.validators.Validator;
import ro.ubb.opera.core.repository.Repository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OperaServiceImpl implements OperaService {
    private final Validator<Opera> operaValidator;

    @Autowired
    private final Repository<Integer, Opera> operaRepository;

    public OperaServiceImpl(Repository<Integer, Opera> operaRepository) {
        this.operaValidator = new OperaValidator();
        this.operaRepository = operaRepository;
    }

    @Override
    public Opera addOpera(String title, String language, Integer composerId) {
        Opera opera = new Opera(-1, title, language, composerId);
        this.operaValidator.validate(opera);
        return this.operaRepository.save(opera);
    }

    @Override
    public Opera findOperaById(Integer id) {
        return this.operaRepository.findById(id).orElse(null);
    }

    @Override
    public Opera findOperaByTitle(String title) {
        return this.operaRepository.findAll().stream().filter(opera -> opera.getTitle().equals(title)).findFirst().orElse(null);
    }

    @Override
    public Set<Opera> findOperasByLanguage(String language) {
        return this.operaRepository.findAll().stream().filter(opera -> opera.getLanguage().equals(language)).collect(Collectors.toSet());
    }

    @Override
    public Set<Opera> findOperasByComposer(Integer composerId) {
        return this.operaRepository.findAll().stream().filter(opera -> opera.getComposerId().equals(composerId)).collect(Collectors.toSet());
    }

    @Override
    public Set<Opera> getAllOperas() {
        return new HashSet<>(this.operaRepository.findAll());
    }

    @Override
    @Transactional
    public Opera updateOpera(Integer id, String title, String language, Integer composerId) {
        Opera opera = this.operaRepository.findById(id).orElse(null);
        if (opera == null) {
            return null;
        }
        opera.setTitle(title);
        opera.setLanguage(language);
        opera.setComposerId(composerId);
        this.operaValidator.validate(opera);
        return opera;
    }

    @Override
    public Opera deleteOpera(Integer id) {
        Opera opera = this.operaRepository.findById(id).orElse(null);
        this.operaRepository.deleteById(id);
        return opera;
    }
}
