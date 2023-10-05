package ru.pupov.homework05.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import ru.pupov.homework05.domain.Genre;

import java.util.List;

@RequiredArgsConstructor
public class GenreListConverter implements Converter<List<Genre>, String> {

    private final ObjectMapper mapper;

    @Override
    public String convert(List<Genre> sourceList) {
        try {
            return mapper.writeValueAsString(sourceList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("failed to convert entity");
        }
    }
}
