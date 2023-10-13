package ru.pupov.homework07.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import ru.pupov.homework07.dto.GenreDto;

import java.util.List;

@RequiredArgsConstructor
public class GenreDtoListConverter implements Converter<List<GenreDto>, String> {

    private final ObjectMapper mapper;

    @Override
    public String convert(List<GenreDto> sourceList) {
        try {
            return mapper.writeValueAsString(sourceList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("failed to convert entity");
        }
    }
}
