package ro.ubb.opera.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.ubb.opera.core.service.ComposerService;
import ro.ubb.opera.web.converter.ComposerConverter;
import ro.ubb.opera.web.dto.ComposersDto;

import java.util.concurrent.ExecutionException;

@RestController
public class Controller {
    @Autowired
    private ComposerService composerService;

    @Autowired
    private ComposerConverter composerConverter;

    @RequestMapping(value="/composers")
    ComposersDto getAllComposers() throws ExecutionException, InterruptedException {
        return new ComposersDto(this.composerConverter.convertModelsToDtos(this.composerService.getAllComposers()));
    }
}
