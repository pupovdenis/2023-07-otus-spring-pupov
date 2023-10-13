package ru.pupov.homework07.mapper;

import org.springframework.stereotype.Component;
import ru.pupov.homework07.dto.AuthorDto;
import ru.pupov.homework07.entity.Author;

import java.util.List;

@Component
public class AuthorMapper {

    public List<AuthorDto> toDtoList(List<Author> authors) {
        return authors.stream()
                .map(this::toDto)
                .toList();
    }

    public AuthorDto toDto(Author author) {
        return AuthorDto.builder()
                .id(author.getId())
                .firstname(author.getFirstname())
                .lastname(author.getLastname())
                .build();
    }
}
