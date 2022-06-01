package ro.ubb.opera.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.opera.core.model.Orchestra;
import ro.ubb.opera.core.model.validators.OrchestraValidator;
import ro.ubb.opera.core.model.validators.Validator;
import ro.ubb.opera.core.repository.Repository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrchestraServiceImpl implements OrchestraService {
    private final Validator<Orchestra> orchestraValidator;

    @Autowired
    private final Repository<Integer, Orchestra> orchestraRepository;

    public OrchestraServiceImpl(Repository<Integer, Orchestra> orchestraRepository) {
        this.orchestraValidator = new OrchestraValidator();
        this.orchestraRepository = orchestraRepository;
    }

    @Override
    public Orchestra addOrchestra(String name, String conductor) {
        Orchestra orchestra = new Orchestra(-1, name, conductor);
        this.orchestraValidator.validate(orchestra);
        return this.orchestraRepository.save(orchestra);
    }

    @Override
    public Orchestra findOrchestraById(Integer id) {
        return this.orchestraRepository.findById(id).orElse(null);
    }

    @Override
    public Orchestra findOrchestraByName(String name) {
        return this.orchestraRepository.findAll().stream().filter(orchestra -> orchestra.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public Set<Orchestra> findOrchestrasByConductor(String conductor) {
        return this.orchestraRepository.findAll().stream().filter(orchestra -> orchestra.getConductor().equals(conductor)).collect(Collectors.toSet());
    }

    @Override
    public Set<Orchestra> getAllOrchestras() {
        return new HashSet<>(this.orchestraRepository.findAll());
    }

    @Override
    @Transactional
    public Orchestra updateOrchestra(Integer id, String name, String conductor) {
        Orchestra orchestra = this.orchestraRepository.findById(id).orElse(null);
        if (orchestra == null) {
            return null;
        }
        orchestra.setName(name);
        orchestra.setConductor(conductor);
        this.orchestraValidator.validate(orchestra);
        return orchestra;
    }

    @Override
    public Orchestra deleteOrchestra(Integer id) {
        Orchestra orchestra = this.orchestraRepository.findById(id).orElse(null);
        this.orchestraRepository.deleteById(id);
        return orchestra;
    }
}
