package ro.ubb.opera.web.converter;

import ro.ubb.opera.core.model.BaseEntity;
import ro.ubb.opera.web.dto.BaseDto;

public interface Converter<Model extends BaseEntity<Integer>, Dto extends BaseDto> {
    Model convertDtoToModel(Dto dto);

    Dto convertModelToDto(Model model);
}