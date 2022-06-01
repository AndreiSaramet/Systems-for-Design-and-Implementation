package ro.ubb.opera.web.converter;

import ro.ubb.opera.core.model.Rehearsal;
import ro.ubb.opera.web.dto.RehearsalDto;

public class RehearsalConverter extends BaseConverter<Rehearsal, RehearsalDto> {
    @Override
    public Rehearsal convertDtoToModel(RehearsalDto rehearsalDto) {
        Rehearsal rehearsal = new Rehearsal();
        rehearsal.setId(rehearsalDto.getId());
        rehearsal.setOrchestraId(rehearsalDto.getOrchestraId());
        rehearsal.setVenueId(rehearsalDto.getVenueId());
        return rehearsal;
    }

    @Override
    public RehearsalDto convertModelToDto(Rehearsal rehearsal) {
        RehearsalDto rehearsalDto = new RehearsalDto(rehearsal.getOrchestraId(), rehearsal.getVenueId());
        rehearsalDto.setId(rehearsal.getId());
        return rehearsalDto;
    }
}
