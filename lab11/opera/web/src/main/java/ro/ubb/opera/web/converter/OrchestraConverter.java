package ro.ubb.opera.web.converter;

import ro.ubb.opera.core.model.Orchestra;
import ro.ubb.opera.web.dto.OrchestraDto;

public class OrchestraConverter extends BaseConverter<Orchestra, OrchestraDto> {
    @Override
    public Orchestra convertDtoToModel(OrchestraDto orchestraDto) {
        Orchestra orchestra = new Orchestra();
        orchestra.setId(orchestraDto.getId());
        orchestra.setName(orchestraDto.getName());
        orchestra.setConductor(orchestraDto.getConductor());
        return orchestra;
    }

    @Override
    public OrchestraDto convertModelToDto(Orchestra orchestra) {
        OrchestraDto orchestraDto = new OrchestraDto(orchestra.getName(), orchestra.getConductor());
        orchestraDto.setId(orchestra.getId());
        return orchestraDto;
    }
}
