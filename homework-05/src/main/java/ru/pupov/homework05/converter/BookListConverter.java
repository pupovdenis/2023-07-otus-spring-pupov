package ru.pupov.homework05.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import ru.pupov.homework05.domain.Book;

import java.util.List;

@RequiredArgsConstructor
public class BookListConverter implements Converter<List<Book>, String> {

    private final ObjectMapper mapper;

    @Override
    public String convert(List<Book> sourceList) {
        try {
            return mapper.writeValueAsString(sourceList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("failed to convert entity");
        }
    }
}
