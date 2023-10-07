package ru.pupov.homework06.mapper;

import org.springframework.stereotype.Component;
import ru.pupov.homework06.dto.AuthorDto;
import ru.pupov.homework06.entity.Author;

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
