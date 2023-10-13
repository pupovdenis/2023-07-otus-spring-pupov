package ru.pupov.homework06.mapper;

import org.springframework.stereotype.Component;
import ru.pupov.homework06.dto.GenreDto;
import ru.pupov.homework06.entity.Genre;

import java.util.List;

@Component
public class GenreMapper {

    public List<GenreDto> toDtoList(List<Genre> genres) {
        return genres.stream()
                .map(this::toDto)
                .toList();
    }

    public GenreDto toDto(Genre genre) {
        return GenreDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }
}
