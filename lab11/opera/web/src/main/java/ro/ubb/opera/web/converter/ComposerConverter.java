package ro.ubb.opera.web.converter;

import org.springframework.stereotype.Component;
import ro.ubb.opera.core.model.Composer;
import ro.ubb.opera.web.dto.ComposerDto;
import ro.ubb.opera.web.dto.ComposersDto;

@Component
public class ComposerConverter extends BaseConverter<Composer, ComposerDto> {
    @Override
    public Composer convertDtoToModel(ComposerDto dto) {
        Composer composer = new Composer();
        composer.setId(dto.getId());
        composer.setName(dto.getName());
        composer.setNationality(dto.getNationality());
        composer.setMusicalPeriod(dto.getMusicalPeriod());
        return composer;
    }

    @Override
    public ComposerDto convertModelToDto(Composer composer) {
        ComposerDto composerDto = new ComposerDto(composer.getName(), composer.getNationality(), composer.getMusicalPeriod());
        composerDto.setId(composer.getId());
        return composerDto;
    }
}
