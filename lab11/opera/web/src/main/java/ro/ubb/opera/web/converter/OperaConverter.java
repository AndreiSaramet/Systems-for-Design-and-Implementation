package ro.ubb.opera.web.converter;

import ro.ubb.opera.core.model.Opera;
import ro.ubb.opera.web.dto.OperaDto;

public class OperaConverter extends BaseConverter<Opera, OperaDto> {
    @Override
    public Opera convertDtoToModel(OperaDto operaDto) {
        Opera opera = new Opera();
        opera.setId(operaDto.getId());
        opera.setTitle(operaDto.getTitle());
        opera.setLanguage(operaDto.getLanguage());
        opera.setComposerId(operaDto.getComposerId());
        return opera;
    }

    @Override
    public OperaDto convertModelToDto(Opera opera) {
        OperaDto operaDto = new OperaDto(opera.getTitle(), opera.getLanguage(), opera.getComposerId());
        operaDto.setId(opera.getId());
        return operaDto;
    }
}
