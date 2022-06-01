package ro.ubb.opera.web.converter;

import org.springframework.stereotype.Component;
import ro.ubb.opera.core.model.Composer;
import ro.ubb.opera.web.dto.ComposerDto;

@Component
public class ComposerConverter extends BaseConverter<Composer, ComposerDto> {
    @Override
    public Composer convertDtoToModel(ComposerDto composerDto) {
        Composer composer = new Composer();
        composer.setId(composerDto.getId());
        composer.setName(composerDto.getName());
        composer.setNationality(composerDto.getNationality());
        composer.setMusicalPeriod(composerDto.getMusicalPeriod());
        return composer;
    }

    @Override
    public ComposerDto convertModelToDto(Composer composer) {
        ComposerDto composerDto = new ComposerDto(composer.getName(), composer.getNationality(), composer.getMusicalPeriod());
        composerDto.setId(composer.getId());
        return composerDto;
    }
}
