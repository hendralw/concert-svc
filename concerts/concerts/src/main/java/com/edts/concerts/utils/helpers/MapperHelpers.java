package com.edts.concerts.utils.helpers;

import com.edts.concerts.utils.exceptions.MappingErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class MapperHelpers {
    private final ModelMapper modelMapper;

    public <T,E> T toDto(E entity, Class<T> dtoClass) {
        try {
            return modelMapper.map(entity, dtoClass);
        } catch (Exception ex) {
            log.error("Mapping error occurred: {} to {}", entity.getClass().getSimpleName(), dtoClass.getSimpleName(), ex);
            throw new MappingErrorException("An error occurred while mapping", ex);
        }
    }

    public <T, E> List<T> toDtoList(List<E> entities, Class<T> dtoClass) {
        try {
            return entities.stream()
                    .map(entity -> modelMapper.map(entity, dtoClass))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("Mapping error occurred: List of {} entities to List of {} DTOs", entities.size(), dtoClass.getSimpleName(), ex);
            throw new MappingErrorException("An error occurred while mapping", ex);
        }
    }
}
