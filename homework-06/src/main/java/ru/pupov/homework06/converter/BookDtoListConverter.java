package ru.pupov.homework06.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import ru.pupov.homework06.dto.BookDto;

import java.util.List;

@RequiredArgsConstructor
public class BookDtoListConverter implements Converter<List<BookDto>, String> {

    private final ObjectMapper mapper;

    @Override
    public String convert(List<BookDto> sourceList) {
        try {
            return mapper.writeValueAsString(sourceList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("failed to convert entity");
        }
    }
}
