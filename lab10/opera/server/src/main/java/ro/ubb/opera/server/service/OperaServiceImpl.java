package ro.ubb.opera.server.service;

import ro.ubb.opera.common.domain.Opera;
import ro.ubb.opera.common.domain.validators.Validator;
import ro.ubb.opera.common.service.OperaService;
import ro.ubb.opera.server.repository.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class OperaServiceImpl implements OperaService {
    private final ExecutorService executorService;

    private final Validator<Opera> operaValidator;

    private final Repository<Integer, Opera> operaRepository;

    public OperaServiceImpl(ExecutorService executorService, Validator<Opera> operaValidator, Repository<Integer, Opera> operaRepository) {
        this.executorService = executorService;
        this.operaValidator = operaValidator;
        this.operaRepository = operaRepository;
    }

    @Override
    public Future<Opera> addOpera(String title, String language, Integer composerId) {
        return this.executorService.submit(() -> {
            Opera opera = new Opera(-1, title, language, composerId);
            this.operaValidator.validate(opera);
            return this.operaRepository.save(opera).orElse(null);
        });
    }

    @Override
    public Future<Opera> findOperaById(Integer id) {
        return this.executorService.submit(() -> this.operaRepository.findOne(id).orElse(null));
    }

    @Override
    public Future<Opera> findOperaByTitle(String title) {
        return this.executorService.submit(() -> {
            Set<Opera> operaSet = new HashSet<>();
            this.operaRepository.findAll().forEach(operaSet::add);
            operaSet = operaSet.stream().filter(opera -> opera.getTitle().equals(title)).collect(Collectors.toSet());
            if (operaSet.isEmpty()) {
                return null;
            }
            return new ArrayList<>(operaSet).get(0);
        });
    }

    @Override
    public Future<Set<Opera>> findOperasByLanguage(String language) {
        return this.executorService.submit(() -> {
            Set<Opera> operaSet = new HashSet<>();
            this.operaRepository.findAll().forEach(operaSet::add);
            return operaSet.stream().filter(opera -> opera.getLanguage().equals(language)).collect(Collectors.toSet());
        });
    }

    @Override
    public Future<Set<Opera>> findOperasByComposer(Integer composerId) {
        return this.executorService.submit(() -> {
            Set<Opera> operaSet = new HashSet<>();
            this.operaRepository.findAll().forEach(operaSet::add);
            return operaSet.stream().filter(opera -> opera.getComposerId().equals(composerId)).collect(Collectors.toSet());
        });
    }

    @Override
    public Future<Set<Opera>> getAllOperas() {
        return this.executorService.submit(() -> {
            Set<Opera> operaSet = new HashSet<>();
            this.operaRepository.findAll().forEach(operaSet::add);
            return operaSet;
        });
    }

    @Override
    public Future<Opera> updateOpera(Integer id, String title, String language, Integer composerId) {
        return this.executorService.submit(() -> {
            Opera opera = new Opera(id, title, language, composerId);
            this.operaValidator.validate(opera);
            return this.operaRepository.update(opera).orElse(null);
        });
    }

    @Override
    public Future<Opera> deleteOpera(Integer id) {
        return this.executorService.submit(() -> this.operaRepository.delete(id).orElse(null));
    }
}
