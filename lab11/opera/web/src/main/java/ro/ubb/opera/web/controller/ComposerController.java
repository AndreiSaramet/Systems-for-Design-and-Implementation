package ro.ubb.opera.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.ubb.opera.core.service.ComposerService;
import ro.ubb.opera.web.converter.ComposerConverter;
import ro.ubb.opera.web.dto.ComposerDto;
import ro.ubb.opera.web.dto.ComposersDto;
import ro.ubb.opera.web.dto.ComposersOrderedDto;

@RestController
public class ComposerController {
    @Autowired
    private ComposerService composerService;

    @Autowired
    private ComposerConverter composerConverter;

    @RequestMapping(value = "/composers", method = RequestMethod.GET)
    public ComposersDto getAllComposers() {
        return new ComposersDto(this.composerConverter.convertModelsToDtos(this.composerService.getAllComposers()));
    }

    @RequestMapping(value = "/composers/{id}", method = RequestMethod.GET)
    public ComposerDto getComposerById(@PathVariable Integer id) {
        return this.composerConverter.convertModelToDto(this.composerService.findComposerById(id));
    }

    @RequestMapping(value = "/composers/name/", method = RequestMethod.GET)
    public ComposerDto getComposerByName(@RequestBody String name) {
        return this.composerConverter.convertModelToDto(this.composerService.findComposerByName(name));
    }

    @RequestMapping(value = "/composers/nationality/", method = RequestMethod.GET)
    public ComposersDto getComposersByNationality(@RequestBody String nationality) {
        return new ComposersDto(this.composerConverter.convertModelsToDtos(this.composerService.findComposersByNationality(nationality)));
    }

    @RequestMapping(value = "/composers/musicalPeriod", method = RequestMethod.GET)
    public ComposersDto getComposersByMusicalPeriod(@RequestBody String musicalPeriod) {
        return new ComposersDto(this.composerConverter.convertModelsToDtos(this.composerService.findComposersByMusicalPeriod(musicalPeriod)));
    }

    @RequestMapping(value = "/composers/sorted/name/asc", method = RequestMethod.GET)
    public ComposersOrderedDto getAllComposersSortedByNameAsc() {
        return new ComposersOrderedDto(this.composerConverter.convertModelsToDtosOrdered(this.composerService.getAllComposersSortedByName(true)));
    }

    @RequestMapping(value = "/composers/sorted/name/desc", method = RequestMethod.GET)
    public ComposersOrderedDto getAllComposersSortedByNameDesc() {
        return new ComposersOrderedDto(this.composerConverter.convertModelsToDtosOrdered(this.composerService.getAllComposersSortedByName(false)));
    }

    @RequestMapping(value = "/composers/sorted/nationality/asc", method = RequestMethod.GET)
    public ComposersOrderedDto getAllComposersSortedByNationalityAsc() {
        return new ComposersOrderedDto(this.composerConverter.convertModelsToDtosOrdered(this.composerService.getAllComposersSortedByNationality(true)));
    }

    @RequestMapping(value = "/composers/sorted/nationality/desc", method = RequestMethod.GET)
    public ComposersOrderedDto getAllComposersSortedByNationalityDesc() {
        return new ComposersOrderedDto(this.composerConverter.convertModelsToDtosOrdered(this.composerService.getAllComposersSortedByNationality(false)));
    }

    @RequestMapping(value = "/composers/sorted/musicalPeriod/asc", method = RequestMethod.GET)
    public ComposersOrderedDto getAllComposersSortedByMusicalPeriodAsc() {
        return new ComposersOrderedDto(this.composerConverter.convertModelsToDtosOrdered(this.composerService.getAllComposersSortedByMusicalPeriod(true)));
    }

    @RequestMapping(value = "/composers/sorted/musicalPeriod/desc", method = RequestMethod.GET)
    public ComposersOrderedDto getAllComposersSortedByMusicalPeriodDesc() {
        return new ComposersOrderedDto(this.composerConverter.convertModelsToDtosOrdered(this.composerService.getAllComposersSortedByMusicalPeriod(false)));
    }

    @RequestMapping(value = "/composers", method = RequestMethod.POST)
    public ComposerDto addComposer(@RequestBody ComposerDto composerDto) {
        return this.composerConverter.convertModelToDto(this.composerService.addComposer(this.composerConverter.convertDtoToModel(composerDto)));
    }

    @RequestMapping(value = "/composers/{id}", method = RequestMethod.PUT)
    public ComposerDto updateComposer(@PathVariable Integer id, @RequestBody ComposerDto composerDto) {
        if (!id.equals(composerDto.getId())) {
            return null;
        }
        return this.composerConverter.convertModelToDto(this.composerService.updateComposer(this.composerConverter.convertDtoToModel(composerDto)));
    }

    @RequestMapping(value = "/composers/{id}", method = RequestMethod.DELETE)
    public ComposerDto deleteComposer(@PathVariable Integer id) {
        return this.composerConverter.convertModelToDto(this.composerService.deleteComposer(id));
    }
}
